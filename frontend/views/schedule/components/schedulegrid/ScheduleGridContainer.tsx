import { useState } from "react";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import {
  CrudAction,
  dateToString,
  dateToStupidDate,
  generateUUID,
  stupidDateToDate,
  stupidDateToString
} from "Frontend/util/utils";
import { ScheduleGrid } from "Frontend/views/schedule/components/schedulegrid/ScheduleGrid";
import { Cell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import SpecificShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/SpecificShiftRequestDTO";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/lib/ConstraintType";
import ScheduleResultDTO from "Frontend/generated/com/cocroachden/planner/solver/ScheduleResultDTO";
import WorkerId from "Frontend/generated/com/cocroachden/planner/lib/WorkerId";

export type Owner = string
export type Index = number

type Row = {
  workerId: Owner
  displayName: string,
  cells: Cell[]
}

type Props = {
  request: PlannerConfigurationDTO
  employees: EmployeeRecord[]
  shiftRequests: SpecificShiftRequestDTO[]
  shiftPerScheduleRequests: ShiftsPerScheduleRequestDTO[]
  onEmployeeAction: (action: CrudAction<EmployeeRecord>) => void
  onShiftRequestsChanged?: (changedRequests: Omit<SpecificShiftRequestDTO, "id">[]) => void
  result?: ScheduleResultDTO
}

type Highlight = {
  originCell: Cell | undefined
  lastCell: Cell | undefined
}

export function ScheduleGridContainer(props: Props) {
  const [highlight, setHighlight] = useState<Highlight>({ originCell: undefined, lastCell: undefined })

  function handleCellLeftClick(cell: Cell) {
    if (highlight.originCell === undefined) {
      setHighlight(() => ({ originCell: cell, lastCell: cell }))
    } else {
      handleGroupShiftChange(highlight.originCell, highlight.lastCell!)
      setHighlight(() => ({ originCell: undefined, lastCell: undefined }))
    }
  }

  function handleCellOnMouseOver(cell: Cell) {
    if (!highlight.originCell) return
    setHighlight(previous => ({ ...previous, lastCell: cell }))
  }

  function handleShiftChange(updatedCell: Cell) {
    props.onShiftRequestsChanged?.([
      {
        type: ConstraintType.SPECIFIC_SHIFT_REQUEST,
        owner: updatedCell.owner,
        date: updatedCell.date,
        requestedShift: updatedCell.shift
      }
    ])
  }

  function handleGroupShiftChange(originCell: Cell, endCell: Cell) {
    const originDate = originCell.index < endCell.index ? stupidDateToDate(originCell.date) : stupidDateToDate(endCell.date)
    const lowEnd = originCell.index < endCell.index ? originCell.index : endCell.index
    const highEnd = originCell.index > endCell.index ? originCell.index : endCell.index
    const requests: Omit<SpecificShiftRequestDTO, "id">[] = []
    for (let i = lowEnd; i <= highEnd; i++) {
      const index = i - lowEnd
      const requestDate = new Date(originDate)
      requestDate.setDate(requestDate.getDate() + index)
      requests.push({
          type: ConstraintType.SPECIFIC_SHIFT_REQUEST,
          owner: originCell.owner,
          requestedShift: originCell.shift,
          date: dateToStupidDate(requestDate)
        } as Omit<SpecificShiftRequestDTO, "id">
      )
    }
    props.onShiftRequestsChanged?.(requests)
  }

  return (
    <ScheduleGrid
      rows={createRows(
        props.request,
        props.employees,
        props.shiftRequests,
        props.shiftPerScheduleRequests,
        highlight,
        props.result
      )}
      onCellChanged={handleShiftChange}
      onMouseOverCell={handleCellOnMouseOver}
      onLeftClick={handleCellLeftClick}
      onEmployeeAction={a => props.onEmployeeAction({
        type: a.type,
        payload: props.employees.find(e => e.workerId === a.payload.workerId)!
      })}
    />
  );
}

function getDistanceInDays(startDate: Date, endDate: Date): Index[] {
  const diffTime = Math.abs(endDate.valueOf() - startDate.valueOf());
  const dayCount = diffTime / (1000 * 60 * 60 * 24)
  const dayIndexes: number[] = []
  for (let i = 0; i <= dayCount; i++) {
    dayIndexes.push(i)
  }
  return dayIndexes
}

function createRows(
  request: PlannerConfigurationDTO,
  employees: EmployeeRecord[],
  shiftRequests: SpecificShiftRequestDTO[],
  shiftPerSchedule: ShiftsPerScheduleRequestDTO[],
  highlightInfo: Highlight,
  results?: ScheduleResultDTO
): Row[] {
  const startDate = stupidDateToDate(request.startDate)
  const endDate = stupidDateToDate(request.endDate)
  const dayIndexes = getDistanceInDays(startDate, endDate)
  return request.workers
    .map(workerId => {
      let highLightIndexes: number[] = []
      if (highlightInfo.originCell && highlightInfo.originCell.owner === workerId.workerId) {
        highLightIndexes = getIndexesBetweenCells(highlightInfo.originCell, highlightInfo.lastCell!)
      }
      const cells = dayIndexes
        .map(dayOffset => {
          const cellDate = new Date(startDate)
          cellDate.setDate(startDate.getDate() + dayOffset)
          const relatedRequest = shiftRequests.find(r => {
            return stupidDateToString(r.date) === dateToString(cellDate) && r.owner === workerId.workerId
          })
          const cellShift = getResultingShift(results, workerId, cellDate, relatedRequest);
          return {
            shift: cellShift,
            index: dayOffset,
            owner: workerId.workerId,
            date: dateToStupidDate(cellDate),
            isHighlighted: highLightIndexes.find(i => i === dayOffset) !== undefined,
            requestId: relatedRequest?.id
          } as Cell
        })
      const referencedEmployee = employees.find(e => e.workerId === workerId.workerId)!
      const relatedShiftPerScheduleRequests = shiftPerSchedule.filter(r => r.owner.workerId === workerId.workerId)
      const displayShiftCount = relatedShiftPerScheduleRequests
        .map(r => Math.floor((r.softMin + r.softMax) / 2))
        .reduce((previousValue, currentValue) => previousValue + currentValue, 0)

      const assignments = results ? Object.values(results?.assignments[workerId.workerId]!) : []

      return {
        workerId: workerId.workerId,
        displayName: getDisplayName(referencedEmployee, displayShiftCount, assignments),
        cells
      } as Row
    })
}

function getDisplayName(
  referencedEmployee: EmployeeRecord,
  displayShiftCount: number,
  assignedWorkShifts: WorkShifts[]
) {
  let title = referencedEmployee.lastName + " " + referencedEmployee.firstName + " (" + displayShiftCount + ")"
  if (assignedWorkShifts.length > 0) {
    const workShifts = assignedWorkShifts.filter(s => s === WorkShifts.DAY || s === WorkShifts.NIGHT);
    title = title + ";" + workShifts.length
  }
  return title;
}

function getResultingShift(
  results: ScheduleResultDTO | undefined,
  workerId: WorkerId,
  cellDate: Date,
  relatedRequest: SpecificShiftRequestDTO | undefined
) {
  if (results) {
    const resultShift = results.assignments[workerId.workerId][dateToString(cellDate)]
    if (resultShift === WorkShifts.OFF) {
      return WorkShifts.ANY
    } else {
      return resultShift || WorkShifts.ANY
    }
  } else {
    if (relatedRequest) {
      return relatedRequest.requestedShift
    } else {
      return WorkShifts.ANY
    }
  }
}

function getIndexesBetweenCells(
  origin: Cell,
  end: Cell
): number[] {
  const [lowEnd, highEnd] = origin.index < end.index
    ? [origin.index, end.index]
    : [end.index, origin.index]
  const indexes = []
  for (let i = lowEnd; i <= highEnd; i++) {
    indexes.push(i)
  }
  return indexes
}

