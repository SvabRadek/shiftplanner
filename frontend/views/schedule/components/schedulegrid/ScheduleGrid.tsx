import { Owner } from "Frontend/views/schedule/components/schedulegrid/ScheduleGridContainer";
import { Cell, GridCell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import { ReactNode } from "react";
import { EmployeeAction, GridNameCell } from "Frontend/views/schedule/components/schedulegrid/GridNameCell";
import { GridHeaderCell } from "Frontend/views/schedule/components/schedulegrid/GridHeaderCell";
import StupidDate from "Frontend/generated/com/cocroachden/planner/configuration/StupidDate";
import { stupidDateToDate } from "Frontend/util/utils";

type Row = {
  workerId: Owner
  displayName: string,
  cells: Cell[]
}


type Props = {
  rows: Row[]
  onCellChanged?: (cell: Cell) => void
  onLeftClick?: (cell: Cell) => void
  onMouseOverCell?: (cell: Cell) => void
  onEmployeeAction?: (action: EmployeeAction) => void
}

export function ScheduleGrid(props: Props) {

  const items = mapToGridCells(props.rows);

  function mapToGridCells(rows: Row[]): ReactNode[] {
    const items: ReactNode[] = []
    rows.forEach((r, rowIndex) => {
      if (rowIndex === 0) {
        items.push(renderCell(
          rowIndex + 1,
          1,
          <GridNameCell
            workerId={r.workerId}
            title={"Jmeno"}
          />
        ))
        r.cells.forEach(c => {
          items.push(renderCell(
            1,
            c.index + 2,
            <GridHeaderCell
              title={c.date.day.toString()}
              backgroundColor={isWeekend(c.date) ? "var(--lumo-shade-20pct)" : undefined}
            />
          ))
        })
      }
      items.push(renderCell(
        rowIndex + 2,
        1,
        <GridNameCell
          title={r.displayName}
          workerId={r.workerId}
          onEmployeeAction={props.onEmployeeAction}
        />
      ))
      return r.cells
        .forEach((c) => {
          items.push(renderCell(
            rowIndex + 2,
            c.index + 2,
            <GridCell
              cell={c}
              onShiftChange={props.onCellChanged}
              onMouseOverCell={props.onMouseOverCell}
              onLeftClick={props.onLeftClick}
              backgroundColor={isWeekend(c.date) ? "var(--lumo-shade-20pct)" : undefined}
            />
          ))
        })
    })
    return items
  }

  return (
    <div style={{
      display: "grid",
      width: "100%",
      overflow: "scroll"
    }}>
      {items}
    </div>
  )
}

function renderCell(
  row: number,
  column: number,
  content: ReactNode
) {
  return (
    <div
      key={"r" + row.toString() + "c" + column.toString()}
      style={{ gridRow: row, gridColumn: column }}
    >
      {content}
    </div>
  )
}

function isWeekend(stupidDate: StupidDate): boolean {
  const date = stupidDateToDate(stupidDate)
  return date.getDay() === 0 || date.getDay() === 6
}


