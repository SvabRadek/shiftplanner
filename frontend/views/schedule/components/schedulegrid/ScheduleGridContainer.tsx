import { useState } from "react";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { dateToString, dateToStupidDate, stupidDateToDate, stupidDateToString } from "Frontend/util/utils";
import { ScheduleGrid } from "Frontend/views/schedule/components/schedulegrid/ScheduleGrid";
import { Cell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import { EmployeeAction } from "Frontend/views/schedule/components/schedulegrid/GridNameCell";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import SpecificShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/SpecificShiftRequestDTO";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/lib/ConstraintType";

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
  onEmployeeAction?: (action: EmployeeAction) => void
  onShiftRequestsChanged?: (changedRequests: SpecificShiftRequestDTO[]) => void
  readonly?: boolean
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
    const requests: SpecificShiftRequestDTO[] = []
    for (let i = lowEnd; i <= highEnd; i++) {
      const index = i - lowEnd
      const requestDate = new Date(originDate)
      requestDate.setDate(requestDate.getDate() + index)
      requests.push({
          type: ConstraintType.SPECIFIC_SHIFT_REQUEST,
          owner: originCell.owner,
          requestedShift: originCell.shift,
          date: dateToStupidDate(requestDate)
        } as SpecificShiftRequestDTO
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
        highlight
      )}
      onCellChanged={handleShiftChange}
      onMouseOverCell={handleCellOnMouseOver}
      onLeftClick={handleCellLeftClick}
      onEmployeeAction={props.onEmployeeAction}
      readonly={props.readonly}
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
  highlightInfo: Highlight
): Row[] {
  const startDate = stupidDateToDate(request.startDate)
  const endDate = stupidDateToDate(request.endDate)
  const dayIndexes = getDistanceInDays(startDate, endDate)
  return request.workers
    .map(w => {
      let highLightIndexes: number[] = []
      if (highlightInfo.originCell && highlightInfo.originCell.owner === w.workerId) {
        highLightIndexes = getIndexesBetweenCells(highlightInfo.originCell, highlightInfo.lastCell!)
      }
      const cells = dayIndexes
        .map(dayOffset => {
          const cellDate = new Date(startDate)
          cellDate.setDate(startDate.getDate() + dayOffset)
          const relatedRequest = shiftRequests.find(r => {
            return stupidDateToString(r.date) === dateToString(cellDate) && r.owner === w.workerId
          })
          return {
            shift: relatedRequest ? relatedRequest.requestedShift : WorkShifts.ANY,
            index: dayOffset,
            owner: w.workerId,
            date: dateToStupidDate(cellDate),
            isHighlighted: highLightIndexes.find(i => i === dayOffset) !== undefined
          } as Cell
        })
      const referencedEmployee = employees.find(e => e.workerId === w.workerId)!
      const relatedShiftPerScheduleRequests = shiftPerSchedule.filter(r => r.owner.workerId === w.workerId)
      const displayShiftCount = relatedShiftPerScheduleRequests
        .map(r => Math.floor((r.softMin + r.softMax) / 2))
        .reduce((previousValue, currentValue) => previousValue + currentValue, 0)

      return {
        workerId: w.workerId,
        displayName: referencedEmployee.lastName + " " + referencedEmployee.firstName + " (" + displayShiftCount + ")",
        cells
      } as Row
    })
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

