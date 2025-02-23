import { ContextMenu, ContextMenuItem, ContextMenuItemSelectedEvent } from "@hilla/react-components/ContextMenu";
import { workShiftBindings } from "Frontend/views/schedule/WorkShiftBindigs";
import { CSSProperties } from "react";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";

export type Cell = {
  shift: WorkShifts
  index: number
  employeeId: string
  date: Date
  isHighlighted: boolean
  constraintId?: string
  backgroundColor?: string
  color?: string
}

type Props = {
  cell: Cell
  onShiftChange?: (cell: Cell) => void
  onLeftClick?: (cell: Cell) => void
  onMouseOverCell?: (cell: Cell) => void
  backgroundColor?: string
  color?: string | undefined
  readonly?: boolean
  style?: CSSProperties
}

function generateCellContextMenuItems(selectedShift: WorkShifts): ContextMenuItem[] {
  return Object.values(workShiftBindings)
    .map(binding => ({
      text: binding.fullText,
      checked: selectedShift === binding.shift
    }))
}

function getWorkShift(fullText: string): WorkShifts {
  return Object.values(workShiftBindings).find(b => b.fullText === fullText)?.shift!
}

export function GridCell(props: Props) {
  const cellContextMenuItems: ContextMenuItem[] = generateCellContextMenuItems(props.cell.shift)

  function handleShiftSelection(e: ContextMenuItemSelectedEvent) {
    const shift = getWorkShift(e.detail.value.text as string)
    props.onShiftChange?.({ ...props.cell, shift: shift })
  }

  function handleLeftClick() {
    if (props.readonly) return
    props.onLeftClick?.(props.cell);
  }

  function handleMouseOver() {
    if (props.readonly) return
    props.onMouseOverCell?.(props.cell)
  }

  const defaultStyle: CSSProperties = {
    display: "flex",
    userSelect: "none",
    width: 50,
    height: 100,
    border: "solid",
    borderColor: "var(--lumo-tint-20pct)",
    borderWidth: "1px",
    justifyContent: "center",
    alignItems: "center",
    color: props.color,
    backgroundColor: props.cell.isHighlighted
      ? "var(--lumo-success-color-10pct)"
      : props.backgroundColor || "var(--lumo-shade-5pct)"
  }

  const cellStyle = Object.assign({}, defaultStyle, props.style)

  function renderCell() {
    return <div
      style={cellStyle}
      onClick={handleLeftClick}
      onMouseOver={handleMouseOver}
    >
      {workShiftBindings[props.cell.shift].symbol}
    </div>
  }

  return (
    props.readonly
      ? renderCell()
      : <ContextMenu items={cellContextMenuItems} onItemSelected={handleShiftSelection}>
        {renderCell()}
      </ContextMenu>
  )
}
