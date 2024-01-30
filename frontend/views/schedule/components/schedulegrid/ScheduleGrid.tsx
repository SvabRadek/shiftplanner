import { Owner } from "Frontend/views/schedule/components/schedulegrid/ScheduleGridContainer";
import { Cell, GridCell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import { ReactNode } from "react";
import { EmployeeAction, GridNameCell } from "Frontend/views/schedule/components/schedulegrid/GridNameCell";
import { GridHeaderCell } from "Frontend/views/schedule/components/schedulegrid/GridHeaderCell";
import { stupidDateToDate, stupidDateToString } from "Frontend/util/utils";
import StupidDate from "Frontend/generated/com/cocroachden/planner/lib/StupidDate";

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
  readonly?: boolean
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
            readonly={true}
          />
        ))
        r.cells.forEach(c => {
          items.push(renderCell(
            1,
            c.index + 2,
            <GridHeaderCell
              title={c.date.day.toString()}
              hint={stupidDateToString(c.date) + ", " + stupidDateToDate(c.date).getDay()}
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
          backgroundColor={props.readonly ? "var(--lumo-shade-20pct)" : undefined }
          readonly={props.readonly}
        />
      ))
      return r.cells
        .forEach((c) => {

          let cellColor = undefined
          if (isWeekend(c.date)) {
            cellColor = "var(--lumo-shade-20pct)"
            if (props.readonly) {
              cellColor = "var(--lumo-shade-30pct)"
            }
          } else {
            if (props.readonly) {
              cellColor = "var(--lumo-shade-20pct)"
            }
          }

          items.push(renderCell(
            rowIndex + 2,
            c.index + 2,
            <GridCell
              cell={c}
              onShiftChange={props.onCellChanged}
              onMouseOverCell={props.onMouseOverCell}
              onLeftClick={props.onLeftClick}
              backgroundColor={cellColor}
              readonly={props.readonly}
            />
          ));
        })
    })
    return items
  }

  return (
    <div style={{
      display: "grid",
      width: "100%",
      overflow: "scroll",
      justifyContent: "start"
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


