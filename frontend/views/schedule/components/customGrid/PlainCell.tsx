import { Cell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import { ContextMenu, ContextMenuItem, ContextMenuItemSelectedEvent } from "@hilla/react-components/ContextMenu";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { workShiftBindings } from "Frontend/views/schedule/WorkShiftBindigs";
import { memo } from "react";

type Props = {
  cell: Cell
  onShiftChange?: (cell: Cell) => void
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

export const PlainCell = memo(function PlainCell(props: Props) {
  const cellContextMenuItems: ContextMenuItem[] = generateCellContextMenuItems(props.cell.shift)

  function handleShiftSelection(e: ContextMenuItemSelectedEvent) {
    const shift = getWorkShift(e.detail.value.text as string)
    props.onShiftChange?.({ ...props.cell, shift: shift })
  }

  return (
    <ContextMenu items={cellContextMenuItems} onItemSelected={handleShiftSelection}>
      <div style={{
        display: "flex",
        userSelect: "none",
        width: 50,
        height: 50,
        border: "solid",
        borderColor: "var(--lumo-tint-20pct)",
        borderWidth: "1px",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "var(--lumo-shade-5pct)"
      }}>
        {workShiftBindings[props.cell.shift].symbol}
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
