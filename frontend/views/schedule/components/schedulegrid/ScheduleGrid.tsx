import { Grid, GridCellPartNameGenerator } from "@hilla/react-components/Grid";
import { GridColumn } from "@hilla/react-components/GridColumn";
import { useEffect, useMemo, useState } from "react";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { Cell, GridCell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { NameCell } from "Frontend/views/schedule/components/schedulegrid/NameCell";
import { EmployeeSelectDialog } from "Frontend/views/schedule/components/EmployeeSelectDialog";
import { dateToStupidDate } from "Frontend/util/utils";
import PlannerConfigurationResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationService/PlannerConfigurationResponse";
import SpecificShiftRequestResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/ConstraintRequestService/SpecificShiftRequestResponse";
import { Operation } from "Frontend/util/types";
import { ContextMenu } from "@hilla/react-components/ContextMenu";
import { PlainGrid } from "Frontend/views/schedule/components/customGrid/PlainGrid";

export type Owner = string
export type Index = number

type Row = {
  workerId: Owner
  displayName: string,
  cells: Cell[]
}

export type HighlightInfo = {
  active: boolean
  owner: string
  startIndex: number
  endIndex: number
  selectedShift: WorkShifts
}

type Props = {
  request: PlannerConfigurationResponse
  employees: EmployeeRecord[]
  shiftRequests: SpecificShiftRequestResponse[]
}

const defaultHighlight = {
  active: false,
  owner: "",
  startIndex: 0,
  endIndex: 0,
  selectedShift: WorkShifts.ANY
}

export function ScheduleGrid(props: Props) {

  const [highlightInfo, setHighlightInfo] = useState<HighlightInfo>(defaultHighlight)
  const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false)
  const [highlightedCells, setHighlightedCells] = useState<Cell[]>([])
  const defaultRows = createRows(props.request, props.employees, props.shiftRequests, highlightedCells)
  const [rows, setRows] = useState<Row[]>(defaultRows)

  console.log("render")

  useEffect(() => {
    if (!highlightInfo.active) setHighlightedCells(() => [])
    const [lowEnd, highEnd] = [highlightInfo.startIndex, highlightInfo.endIndex].sort((a, b) => a - b)
    const indexes = Array.from({ length: highEnd - lowEnd }, (_, index) => lowEnd + index)
    setHighlightedCells(() => {
      return rows.find(r => r.workerId === highlightInfo.owner)?.cells
        .filter(c => indexes.find(i => i === c.index))!
    })
  }, [highlightInfo]);

  const cellPartNameGenerator: GridCellPartNameGenerator<Row> = (column, model) => {
    const startDate = new Date(props.request.startDate);
    let parts = '';
    if (isWeekend(startDate, Number.parseInt(column.header!))) {
      parts += 'weekend';
    }
    return parts;
  };

  function handleEmployeeAdded(employee: EmployeeRecord) {
    setIsAddEmployeeDialogOpen(false)
  }

  function handleCellLeftClick(cell: Cell) {
    if (!highlightInfo.active) {
      setHighlightInfo(() => ({
        active: true,
        owner: cell.owner,
        startIndex: cell.index,
        endIndex: cell.index,
        selectedShift: cell.shift
      }))
    } else {
      copyShiftToCells(highlightInfo.owner, highlightedCells.map(c => c.index), highlightInfo.selectedShift)
      setHighlightInfo((previous) => ({ ...previous, active: false }))
    }
  }

  function handleCellOnMouseOver(cell: Cell) {
    if (!highlightInfo.active) return
    setHighlightInfo(previous => ({
      ...previous!,
      endIndex: cell.index
    }))
  }

  function updateCell(updatedCell: Cell) {
    console.log("updating cell")
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

  function copyShiftToCells(owner: string, indexes: number[], shift: WorkShifts) {
    setRows(previous => {
      return previous.map(row => {
        if (row.workerId !== owner) return row
        return {
          ...row,
          cells: row.cells.map(cell => {
            if (!indexes.find(i => cell.index === i)) return cell
            return { ...cell, isHighlighted: false, shift: shift }
          })
        }
      })
    })
  }

  function renderCell(cell: Cell) {
    return (
      <GridCell
        cell={cell}
        onChange={updateCell}
        onCellLeftClick={handleCellLeftClick}
        onMouseOver={handleCellOnMouseOver}
      />
    )
  }

  function renderName(row: Row) {
    return (
      <NameCell
        key={row.workerId}
        title={row.displayName}
        workerId={row.workerId}
        onContextOperation={(workerId, operation) => {
          if (operation === Operation.ADD) {
            setIsAddEmployeeDialogOpen(true)
            return
          }
          //TODO add remove employee support
        }}
      />
    )
  }

  const cellIndexes = Object.values(rows[0].cells).map(cell => cell.index)

  return (
    <>
      <EmployeeSelectDialog
        employees={props.employees}
        selectedWorkers={props.request.workers}
        onEmployeeSelected={handleEmployeeAdded}
        onOpenChanged={value => setIsAddEmployeeDialogOpen(value)}
        isOpen={isAddEmployeeDialogOpen}
      />
      <PlainGrid rows={rows} onShiftChanged={updateCell} />
      {/*<Grid*/}
      {/*  items={rows}*/}
      {/*  theme={"compact column-borders"}*/}
      {/*  style={{ minHeight: rows.length * 40 }}*/}
      {/*  cellPartNameGenerator={cellPartNameGenerator}*/}
      {/*>*/}
      {/*  <GridColumn width={"200px"} header={"Jmeno"}>*/}
      {/*    {({ item }) => renderName(item)}*/}
      {/*  </GridColumn>*/}
      {/*  {cellIndexes.map(dayIndex => (*/}
      {/*    <GridColumn key={dayIndex} width={"50px"} header={(dayIndex + 1).toString()}>*/}
      {/*      {({ item }) => renderCell(item.cells[dayIndex])}*/}
      {/*    </GridColumn>*/}
      {/*  ))}*/}
      {/*</Grid>*/}
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
  highLightedCells: Cell[] = []
): Row[] {
  const startDate = new Date(request.startDate);
  const endDate = new Date(request.endDate);
  const dayIndexes = getDistanceInDays(startDate, endDate)
  return request.workers
    .map(w => {
      const cells = dayIndexes
        .map(dayOffset => {
          const cellDate = new Date()
          cellDate.setDate(startDate.getDate() + dayOffset)
          const relatedRequest = shiftRequests.find(r => {
            return r.date === dateToStupidDate(cellDate) && r.owner === w.workerId
          })
          const highlightedCell = highLightedCells.find(c => c.owner === w.workerId && c.date === dateToStupidDate(cellDate))
          return {
            shift: relatedRequest ? relatedRequest.requestedShift : WorkShifts.ANY,
            index: dayOffset,
            owner: w.workerId,
            date: dateToStupidDate(cellDate),
            isHighlighted: highlightedCell !== undefined
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

function isWeekend(startDate: Date, offset: number): boolean {
  const columnDate = new Date()
  columnDate.setDate(startDate.getDate() + offset)
  return columnDate.getDay() === 0 || columnDate.getDay() === 6
}
