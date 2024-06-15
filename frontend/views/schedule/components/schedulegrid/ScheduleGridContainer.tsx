import { useMemo, useState } from "react";
import {
  CrudAction,
  dateToString, stringToDate
} from "Frontend/util/utils";
import { Row, ScheduleGrid } from "Frontend/views/schedule/components/schedulegrid/ScheduleGrid";
import { Cell, DisplayMode } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftsPerScheduleRequestDTO";
import ShiftPatternRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftPatternRequestDTO";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/repository/EmployeeRecord";
import SolverConfigurationDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverConfigurationDTO";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverSolutionDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintType";
import EmployeeShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeeShiftRequestDTO";
import EmployeeId from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeId";

export type PlainEmployeeId = number
export type Index = number

type Props = {
  request: SolverConfigurationDTO
  employees: EmployeeRecord[]
  shiftPatterns: ShiftPatternRequestDTO[]
  shiftRequests: EmployeeShiftRequestDTO[]
  shiftPerScheduleRequests: ShiftsPerScheduleRequestDTO[]
  onEmployeeAction: (action: CrudAction<EmployeeRecord>) => void
  onShiftRequestsChanged?: (changedRequests: Omit<EmployeeShiftRequestDTO, "id">[]) => void
  result?: SolverSolutionDTO
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
        type: ConstraintType.EMPLOYEE_SHIFT_REQUEST,
        owner: updatedCell.owner,
        date: dateToString(updatedCell.date),
        requestedShift: updatedCell.shift
      }
    ])
  }

  function handleGroupShiftChange(originCell: Cell, endCell: Cell) {
    const originDate = originCell.index < endCell.index ? originCell.date : endCell.date
    const lowEnd = originCell.index < endCell.index ? originCell.index : endCell.index
    const highEnd = originCell.index > endCell.index ? originCell.index : endCell.index
    const requests: Omit<EmployeeShiftRequestDTO, "id">[] = []
    for (let i = lowEnd; i <= highEnd; i++) {
      const index = i - lowEnd
      const requestDate = new Date(originDate)
      requestDate.setDate(requestDate.getDate() + index)
      requests.push({
          type: ConstraintType.EMPLOYEE_SHIFT_REQUEST,
          owner: originCell.owner,
          requestedShift: originCell.shift,
          date: dateToString(requestDate)
        } as Omit<EmployeeShiftRequestDTO, "id">
      )
    }
    props.onShiftRequestsChanged?.(requests)
  }

  const shiftRequestMap = useMemo(() => {
    const map = new Map<string, EmployeeShiftRequestDTO>()
    props.shiftRequests.forEach(r => map.set(r.date + r.owner.id, r))
    return map
  }, [props.shiftRequests])

  const shiftPatternMap = useMemo(() => {
    const map = new Map<PlainEmployeeId, ShiftPatternRequestDTO>()
    props.shiftPatterns.forEach(r => map.set(r.owner?.id || -1, r))
    return map
  }, [props.shiftPatterns])

  return (
    <ScheduleGrid
      rows={
        createRows(
          props.request,
          props.employees,
          shiftRequestMap,
          shiftPatternMap,
          props.shiftPerScheduleRequests,
          highlight,
          props.result
        )
      }
      onCellChanged={handleShiftChange}
      onMouseOverCell={handleCellOnMouseOver}
      onLeftClick={handleCellLeftClick}
      onEmployeeAction={a => props.onEmployeeAction({
        type: a.type,
        payload: props.employees.find(e => e.id === a.payload.id)!
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
  request: SolverConfigurationDTO,
  employees: EmployeeRecord[],
  shiftRequests: Map<string, EmployeeShiftRequestDTO>,
  shiftPatterns: Map<PlainEmployeeId, ShiftPatternRequestDTO>,
  shiftPerSchedule: ShiftsPerScheduleRequestDTO[],
  highlightInfo: Highlight,
  results?: SolverSolutionDTO
): Row[] {
  const startDate = stringToDate(request.startDate)
  const endDate = stringToDate(request.endDate)
  const dayIndexes = getDistanceInDays(startDate, endDate)
  return request.employees
    .map(employeeId => {
      let highLightIndexes: number[] = []
      if (highlightInfo.originCell && highlightInfo.originCell.owner.id === employeeId.id) {
        highLightIndexes = getIndexesBetweenCells(highlightInfo.originCell, highlightInfo.lastCell!)
      }
      const relatedPattern = shiftPatterns.get(employeeId.id)
      const cells = dayIndexes
        .map(dayOffset => {
          const cellDate = new Date(startDate)
          cellDate.setDate(startDate.getDate() + dayOffset)
          const relatedRequest = shiftRequests.get(dateToString(cellDate) + employeeId.id)
          const cellShift = getResultingShift(
            results,
            employeeId,
            cellDate,
            dayOffset,
            relatedRequest,
            relatedPattern
          );

          return {
            shift: cellShift,
            index: dayOffset,
            owner: employeeId,
            date: cellDate,
            isHighlighted: highLightIndexes.find(i => i === dayOffset) !== undefined,
            requestId: relatedRequest?.id,
            displayMode: results
              ? DisplayMode.DEFAULT
              : relatedRequest
                ? DisplayMode.REQUEST
                : relatedPattern
                  ? DisplayMode.PATTERN
                  : DisplayMode.DEFAULT
          } as Cell
        })
      const referencedEmployee = employees.find(e => e.id === employeeId.id)!
      const relatedShiftPerScheduleRequests = shiftPerSchedule.filter(r => r.owner.id === employeeId.id)
      const displayShiftCount = relatedShiftPerScheduleRequests
        .map(r => Math.floor((r.softMin + r.softMax) / 2))
        .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
      const allowedDeviation = Math.floor(relatedShiftPerScheduleRequests
          .map(r => (r.hardMax - r.softMax))
          .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
        / relatedShiftPerScheduleRequests.length)

      const assignments = results ? Object.values(results?.assignments[employeeId.id]!) : []

      return {
        owner: employeeId,
        rowTitle: createRowTitle(referencedEmployee, displayShiftCount, allowedDeviation, assignments),
        cells,
        issues: []
      } as Row
    })
}

function createRowTitle(
  referencedEmployee: EmployeeRecord,
  displayShiftCount: number,
  allowedDeviation: number,
  assignedWorkShifts: WorkShifts[]
) {
  let title = referencedEmployee.lastName + " " + referencedEmployee.firstName + " (" + displayShiftCount
  const assignedWorkShiftCount = assignedWorkShifts.filter(s => s === WorkShifts.DAY || s === WorkShifts.NIGHT).length
  return (
    <HorizontalLayout theme={"spacing"}
                      style={{ justifyContent: "space-between", width: "100%", alignItems: "baseline" }}>
      <p>{title}<sup> ~{allowedDeviation}</sup>)</p>
      <p>{assignedWorkShiftCount}</p>
    </HorizontalLayout>
  )
}

function getResultingShift(
  results: SolverSolutionDTO | undefined,
  employeeId: EmployeeId,
  cellDate: Date,
  cellIndex: number,
  relatedRequest: EmployeeShiftRequestDTO | undefined,
  relatedPattern: ShiftPatternRequestDTO | undefined
): WorkShifts {
  if (results) {
    const resultShift = results.assignments[employeeId.id][dateToString(cellDate)]
    if (resultShift === WorkShifts.OFF) {
      return WorkShifts.ANY
    } else {
      return resultShift || WorkShifts.ANY
    }
  } else {
    if (relatedRequest) {
      return relatedRequest.requestedShift
    } else {
      if (relatedPattern && relatedPattern.shiftPattern.length > 0) {
        const index = cellIndex + relatedPattern.startDayIndex
        return relatedPattern.shiftPattern[index % relatedPattern.shiftPattern.length]
      } else {
        return WorkShifts.ANY;
      }
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
