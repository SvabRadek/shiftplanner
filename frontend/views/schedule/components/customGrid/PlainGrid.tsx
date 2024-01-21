import { Cell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import { Owner } from "Frontend/views/schedule/components/schedulegrid/ScheduleGrid";
import { PlainCell } from "Frontend/views/schedule/components/customGrid/PlainCell";
import { ReactNode } from "react";
import { NameCell } from "Frontend/views/schedule/components/customGrid/NameCell";
import { HeaderCell } from "Frontend/views/schedule/components/customGrid/HeaderCell";

type Row = {
  workerId: Owner
  displayName: string,
  cells: Cell[]
}


type Props = {
  rows: Row[]
  onShiftChanged?: (cell: Cell) => void
}

export function PlainGrid(props: Props) {

  console.log("render")

  const items = mapToGridCells(props.rows);

  function mapToGridCells(rows: Row[]): ReactNode[] {
    const items: ReactNode[] = []
    rows.forEach((r, rowIndex) => {
      if (rowIndex === 0) {
        items.push(renderCell(
          rowIndex + 1,
          1,
          <NameCell title={"Jmeno"}/>
        ))
        r.cells.forEach(c => {
          items.push(renderCell(
            1,
            c.index + 2,
            <HeaderCell title={c.date.day.toString()}/>
          ))
        })
      }
      items.push(renderCell(
        rowIndex + 2,
        1,
        <NameCell title={r.displayName}/>
      ))
      return r.cells
        .forEach((c) => {
          items.push(renderCell(
            rowIndex + 2,
            c.index + 2,
            <PlainCell
              cell={c}
              onShiftChange={props.onShiftChanged}
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
  );
}

function renderCell(
  row: number,
  column: number,
  content: ReactNode
) {
  return (
    <div
      key={"r" + row.toString() + "c" + column.toString()}
      style={{
        gridRow: row,
        gridColumn: column
      }}
    >
      {content}
    </div>
  )
}


