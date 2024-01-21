import { workShiftBindings } from "Frontend/views/schedule/WorkShiftBindigs";
import { ContextMenu, ContextMenuItem, ContextMenuItemSelectedEvent } from "@hilla/react-components/ContextMenu";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { memo } from "react";
import StupidDate from "Frontend/generated/com/cocroachden/planner/configuration/StupidDate";

export type Cell = {
  shift: WorkShifts
  index: number
  owner: string
  date: StupidDate
  isHighlighted?: boolean
}

type Props = {
  cell: Cell
  onCellLeftClick?: (cell: Cell) => void
  onChange?: (cell: Cell) => void
  onMouseOver?: (cell: Cell) => void
}

function getWorkShift(fullText: string): WorkShifts {
  return Object.values(workShiftBindings).find(b => b.fullText === fullText)?.shift!
}

function generateCellContextMenuItems(selectedShift: WorkShifts): ContextMenuItem[] {
  return Object.values(workShiftBindings)
    .map(binding => ({
      text: binding.fullText,
      checked: selectedShift === binding.shift
    }))
}

export const GridCell = memo(function GridCell(props: Props) {
  const cellContextMenuItems: ContextMenuItem[] = generateCellContextMenuItems(props.cell.shift)

  function handleShiftSelection(e: ContextMenuItemSelectedEvent) {
    const shift = getWorkShift(e.detail.value.text as string)
    props.onChange?.({ ...props.cell, shift: shift })
  }

  return (
    <ContextMenu items={cellContextMenuItems} onItemSelected={handleShiftSelection}>
      <div
        key={props.cell.index + props.cell.owner}
        style={{
          borderRadius: "var(--lumo-border-radius-m)",
          alignItems: "center",
          justifyContent: "center",
          display: "flex",
          minHeight: 40,
          backgroundColor: props.cell.isHighlighted ? "var(--lumo-success-color-50pct)" : undefined,
          userSelect: "none"
        }}
        onClick={() => props.onCellLeftClick?.(props.cell)}
        onMouseEnter={() => props.onMouseOver?.(props.cell)}
      >
        <span style={{ fontSize: 30 }}>
          {workShiftBindings[props.cell.shift].symbol}
        </span>
      </div>
    </ContextMenu>

  );
}, (prevProps, nextProps) => {
  return prevProps.cell.owner === nextProps.cell.owner
    && prevProps.cell.index === nextProps.cell.index
    && prevProps.cell.isHighlighted === nextProps.cell.isHighlighted
    && prevProps.cell.date === nextProps.cell.date
    && prevProps.cell.shift === nextProps.cell.shift
})
