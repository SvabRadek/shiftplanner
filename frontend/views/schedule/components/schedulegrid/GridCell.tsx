import { ContextMenu, ContextMenuItem, ContextMenuItemSelectedEvent } from "@hilla/react-components/ContextMenu";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { workShiftBindings } from "Frontend/views/schedule/WorkShiftBindigs";
import StupidDate from "Frontend/generated/com/cocroachden/planner/configuration/StupidDate";

export type Cell = {
  shift: WorkShifts
  index: number
  owner: string
  date: StupidDate
  isHighlighted: boolean
}

type Props = {
  cell: Cell
  onShiftChange?: (cell: Cell) => void
  onLeftClick?: (cell: Cell) => void
  onMouseOverCell?: (cell: Cell) => void
  backgroundColor?: string
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
    props.onLeftClick?.(props.cell)
  }

  function handleMouseOver() {
    props.onMouseOverCell?.(props.cell)
  }

  return (
    <ContextMenu items={cellContextMenuItems} onItemSelected={handleShiftSelection}>
      <div
        style={{
          display: "flex",
          userSelect: "none",
          width: 50,
          height: 50,
          border: "solid",
          borderColor: "var(--lumo-tint-20pct)",
          borderWidth: "1px",
          justifyContent: "center",
          alignItems: "center",
          backgroundColor: props.cell.isHighlighted
            ? "var(--lumo-success-color-10pct)"
            : props.backgroundColor || "var(--lumo-shade-5pct)"
        }}
        onClick={handleLeftClick}
        onMouseOver={handleMouseOver}
      >
        {workShiftBindings[props.cell.shift].symbol}
      </div>
    </ContextMenu>
  );
}
