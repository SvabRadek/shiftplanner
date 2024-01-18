import { Grid, GridCellPartNameGenerator } from "@hilla/react-components/Grid";
import { GridColumn } from "@hilla/react-components/GridColumn";
import { useContext, useEffect, useState } from "react";
import { ConstraintRequestService } from "Frontend/generated/endpoints";
import SpecificShiftRequestResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/ConstraintRequestService/SpecificShiftRequestResponse";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { Cell, GridCell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import { HighlightCxtProvider } from "Frontend/views/schedule/components/schedulegrid/HighlightCxtProvider";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { NameCell } from "Frontend/views/schedule/components/schedulegrid/NameCell";
import {
  Index,
  Owner,
  RequestCtx,
  RequestCtxType
} from "Frontend/views/schedule/components/schedulegrid/RequestCtxProvider";
import { EmployeeSelectDialog } from "Frontend/views/schedule/components/EmployeeSelectDialog";
import { dateToStupidDate, getDistanceInDaysNumeric } from "Frontend/util/utils";

type GridModel = Record<Owner, Row>

type Row = {
  workerId: Owner
  displayName: string,
  cells: Record<Index, Cell>
}


export function ScheduleGrid() {

  const requestCtx = useContext(RequestCtx)
  if (!requestCtx.request) return (<div>No data!</div>)

  const [gridRows, setGridRows] = useState<GridModel>(generateRows(requestCtx))
  const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false)

  useEffect(() => {
      setGridRows(generateRows(requestCtx));
  }, [requestCtx.request, requestCtx.specificShiftRequests]);

  const cellPartNameGenerator: GridCellPartNameGenerator<Row> = (column, model) => {
    const startDate = new Date(requestCtx.request!.startDate);
    let parts = '';
    if (isWeekend(startDate, Number.parseInt(column.header!))) {
      parts += 'weekend';
    }
    return parts;
  };

  function handleEmployeeAdded(employee: EmployeeRecord) {
    requestCtx.addEmployeeToRequest(employee.workerId)
    setIsAddEmployeeDialogOpen(false)
  }

  function renderCell(cell: Cell) {
    return (
      <GridCell cell={cell} onChange={state => requestCtx.addSpecificShiftRequest(state.shift, state.owner, state.date)}/>
    )
  }

  function renderName(row: Row) {
    return (
      <NameCell
        key={row.workerId}
        title={row.displayName}
        workerId={row.workerId}
        onAddEmployee={() => setIsAddEmployeeDialogOpen(true)}
      />
    )
  }

  const rows = Object.values(gridRows)
  const cellIndexes = Object.values(rows[0].cells).map(cell => cell.index)

  return (
    <>
      <EmployeeSelectDialog onEmployeeSelected={handleEmployeeAdded} onOpenChanged={value => setIsAddEmployeeDialogOpen(value)} isOpen={isAddEmployeeDialogOpen}/>
      <HighlightCxtProvider>
        <Grid
          items={Object.values(gridRows)}
          theme={"compact column-borders"}
          style={{ minHeight: rows.length * 40 }}
          cellPartNameGenerator={cellPartNameGenerator}
        >
          <GridColumn width={"200px"} header={"Jmeno"}>
            {({ item }) => renderName(item)}
          </GridColumn>
          {cellIndexes.map(dayIndex => (
            <GridColumn key={dayIndex} width={"50px"} header={(dayIndex + 1).toString()}>
              {({ item }) => renderCell(item.cells[dayIndex])}
            </GridColumn>
          ))}
        </Grid>
      </HighlightCxtProvider>
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

function generateRows(
  requestCtx: RequestCtxType
): GridModel {
  if (!requestCtx.request) return {}
  const startDate = new Date(requestCtx.request.startDate);
  const endDate = new Date(requestCtx.request.endDate);
  const dayIndexes = getDistanceInDays(startDate, endDate)
  const rows: GridModel = {}
  requestCtx.request.workers
    .map(w => {
      const cells: Row["cells"] = {}
      dayIndexes
        .map(day => {
          const cellDate = new Date()
          cellDate.setDate(startDate.getDate() + day)
          return {
            shift: WorkShifts.ANY,
            index: day,
            owner: w.workerId,
            date: dateToStupidDate(cellDate)
          } as Cell
        }).forEach(cell => cells[cell.index] = cell)

      requestCtx.specificShiftRequests
        .filter(r => r.owner === w.workerId)
        .forEach(r => {
          const rDate = new Date(r.date.year, r.date.month, r.date.day)
          const index = getDistanceInDaysNumeric(startDate, rDate)
          const cell: Cell = { shift: r.requestedShift, index, owner: w.workerId, date: r.date }
          if (index >= 0) cells[cell.index] = cell
        })
      const referencedEmployee = requestCtx.allEmployees.find(e => e.workerId === w.workerId)!
      return {
        workerId: w.workerId,
        displayName: referencedEmployee.lastName + " " + referencedEmployee.firstName,
        cells: cells
      } as Row
    }).forEach(gr => rows[gr.workerId] = gr)
  return rows
}

function isWeekend(startDate: Date, offset: number): boolean {
  const columnDate = new Date()
  columnDate.setDate(startDate.getDate() + offset)
  return columnDate.getDay() === 0 || columnDate.getDay() === 6
}
