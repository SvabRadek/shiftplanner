import { useEffect, useState } from "react";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { EmployeeSelectDialog } from "Frontend/views/schedule/components/EmployeeSelectDialog";
import { dateToStupidDate } from "Frontend/util/utils";
import PlannerConfigurationResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationService/PlannerConfigurationResponse";
import SpecificShiftRequestResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/ConstraintRequestService/SpecificShiftRequestResponse";
import { PlainGrid } from "Frontend/views/schedule/components/schedulegrid/PlainGrid";
import { Cell } from "Frontend/views/schedule/components/schedulegrid/PlainCell";

export type Owner = string
export type Index = number

type Row = {
  workerId: Owner
  displayName: string,
  cells: Cell[]
}

type Props = {
  request: PlannerConfigurationResponse
  employees: EmployeeRecord[]
  shiftRequests: SpecificShiftRequestResponse[]
}

type Highlight = {
  originCell: Cell | undefined
  lastCell: Cell | undefined
}

export function ScheduleGridContainer(props: Props) {

  const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false)
  const [highlight, setHighlight] = useState<Highlight>({ originCell: undefined, lastCell: undefined })
  const defaultRows = createRows(props.request, props.employees, props.shiftRequests, highlight)
  const [rows, setRows] = useState<Row[]>(defaultRows)

  console.log("render")

  useEffect(() => {
    setRows(previous => {
      return updateHighlightForRows(previous, highlight)
    })
  }, [highlight]);

  function handleEmployeeAdded(employee: EmployeeRecord) {
    setIsAddEmployeeDialogOpen(false)
  }

  function handleCellLeftClick(cell: Cell) {
    if (highlight.originCell === undefined) {
      setHighlight({
        originCell: cell,
        lastCell: cell
      })
    } else {
      copyShiftToCells(highlight.originCell, highlight.lastCell!)
      setHighlight(() => ({
        originCell: undefined,
        lastCell: undefined
      }))
    }
  }

  function handleCellOnMouseOver(cell: Cell) {
    if (!highlight.originCell) return
    setHighlight(previous => ({
      ...previous,
      lastCell: cell
    }))
  }

  function updateCell(updatedCell: Cell) {
    setRows(previous => {
      return previous.map(row => {
        if (row.workerId !== updatedCell.owner) return row
        return {
          ...row,
          cells: row.cells.map(cell => {
            if (cell.date !== updatedCell.date) return cell
            return updatedCell
          })
        }
      })
    })
  }

  function copyShiftToCells(originCell: Cell, endCell: Cell) {
    console.log([originCell, endCell])
    const indexes = getIndexesBetweenCells(originCell, endCell)
    console.log("indexes to copy: " + indexes)
    setRows(previous => {
      return previous.map(row => {
        if (row.workerId !== originCell.owner) return row
        return {
          ...row,
          cells: row.cells.map(cell => {
            if (!indexes.find(i => cell.index === i)) return cell
            return { ...cell, isHighlighted: false, shift: originCell.shift }
          })
        }
      })
    })
  }

  return (
    <>
      <EmployeeSelectDialog
        employees={props.employees}
        selectedWorkers={props.request.workers}
        onEmployeeSelected={handleEmployeeAdded}
        onOpenChanged={value => setIsAddEmployeeDialogOpen(value)}
        isOpen={isAddEmployeeDialogOpen}
      />
      <PlainGrid
        rows={rows}
        onCellChanged={updateCell}
        onMouseOverCell={handleCellOnMouseOver}
        onLeftClick={handleCellLeftClick}
      />
    </>
  );
}

function getDistanceInDays(startDate: Date, endDate: Date): Index[] {
  const diffTime = Math.abs(endDate.valueOf() - startDate.valueOf());
  const dayCount = diffTime / (1000 * 60 * 60 * 24)
  const dayIndexes: number[] = []
  for (let i = 0; i < dayCount; i++) {
    dayIndexes.push(i)
  }
  return dayIndexes
}

function createRows(
  request: PlannerConfigurationResponse,
  employees: EmployeeRecord[],
  shiftRequests: SpecificShiftRequestResponse[],
  highlightInfo: Highlight
): Row[] {
  const startDate = new Date(request.startDate);
  const endDate = new Date(request.endDate);
  const dayIndexes = getDistanceInDays(startDate, endDate)
  return request.workers
    .map(w => {
      let highLightIndexes: number[] = []
      if (highlightInfo.originCell && highlightInfo.originCell.owner === w.workerId) {
        highLightIndexes = getIndexesBetweenCells(highlightInfo.originCell, highlightInfo.lastCell!)
      }
      const cells = dayIndexes
        .map(dayOffset => {
          const cellDate = new Date()
          cellDate.setDate(startDate.getDate() + dayOffset)
          const relatedRequest = shiftRequests.find(r => {
            return r.date === dateToStupidDate(cellDate) && r.owner === w.workerId
          })
          return {
            shift: relatedRequest ? relatedRequest.requestedShift : WorkShifts.ANY,
            index: dayOffset,
            owner: w.workerId,
            date: dateToStupidDate(cellDate),
            isHighlighted: !!highLightIndexes.find(i => i === dayOffset)
          } as Cell
        })
      const referencedEmployee = employees.find(e => e.workerId === w.workerId)!
      return {
        workerId: w.workerId,
        displayName: referencedEmployee.lastName + " " + referencedEmployee.firstName,
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

function updateHighlightForRows(
  rows: Row[],
  highlight: Highlight
): Row[] {
  if (!highlight.originCell || !highlight.lastCell) return rows
  const highlightIndexes = getIndexesBetweenCells(highlight.originCell, highlight.lastCell)
  return rows.map(row => {
    if (row.workerId !== highlight.originCell?.owner) return row
    return {
      ...row,
      cells: row.cells.map(cell => {
        if (highlightIndexes.find(i => cell.index === i)) {
          return { ...cell, isHighlighted: true }
        } else {
          return { ...cell, isHighlighted: false };
        }
      })
    }
  })
}

function isWeekend(startDate: Date, offset: number): boolean {
  const columnDate = new Date()
  columnDate.setDate(startDate.getDate() + offset)
  return columnDate.getDay() === 0 || columnDate.getDay() === 6
}
