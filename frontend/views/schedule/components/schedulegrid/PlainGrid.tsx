import { Owner } from "Frontend/views/schedule/components/schedulegrid/ScheduleGridContainer";
import { Cell, PlainCell } from "Frontend/views/schedule/components/schedulegrid/PlainCell";
import { ReactNode } from "react";
import { NameCell } from "Frontend/views/schedule/components/schedulegrid/NameCell";
import { HeaderCell } from "Frontend/views/schedule/components/schedulegrid/HeaderCell";

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
}

export function PlainGrid(props: Props) {

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
              onShiftChange={props.onCellChanged}
              onMouseOverCell={props.onMouseOverCell}
              onLeftClick={props.onLeftClick}
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


