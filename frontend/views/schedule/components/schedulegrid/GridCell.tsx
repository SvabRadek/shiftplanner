import { workShiftBindings } from "Frontend/views/schedule/WorkShiftBindigs";
import { ContextMenu, ContextMenuItem, ContextMenuItemSelectedEvent } from "@hilla/react-components/ContextMenu";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { useContext, useEffect, useState } from "react";
import {
  HighlightContext,
  HighlightInfo
} from "Frontend/views/schedule/components/schedulegrid/HighlightCxtProvider";
import StupidDate from "Frontend/generated/com/cocroachden/planner/configuration/StupidDate";

export type Cell = {
  shift: WorkShifts
  index: number
  owner: string
  date: StupidDate
}

type Props = {
  cell: Cell
  onCellLeftClick?: (cell: Cell) => void
  onChange?: (cell: Cell) => void
}

function getWorkShift(fullText: string): WorkShifts {
  return Object.values(workShiftBindings)
    .find(b => b.fullText === fullText)?.shift!
}

function generateCellContextMenuItems(selectedShift: WorkShifts): ContextMenuItem[] {
  return Object.values(workShiftBindings)
    .map(binding => ({
      text: binding.fullText,
      checked: selectedShift === binding.shift
    }))
}

function shouldCellBeHighlighted(highlightInfo: HighlightInfo | undefined, cell: Cell): boolean {
  if (!highlightInfo) return false
  if (highlightInfo.owner !== cell.owner) return false
  return !!highlightInfo.indexes.find(i => i === cell.index);
}

export function GridCell(props: Props) {
  const highlightCtx = useContext(HighlightContext)
  const [isHighlighted, setIsHighlighted] = useState(false)
  const [cellState, setCellState] = useState<Cell>(props.cell)
  const cellContextMenuItems: ContextMenuItem[] = generateCellContextMenuItems(props.cell.shift)

  useEffect(() => {
    props.onChange?.(cellState)
  }, [cellState]);

  useEffect(() => {
    if (!highlightCtx.highlightInfo && isHighlighted) {
      setCellState(old => ({ ...old, shift: highlightCtx.originShift }))
    }
    setIsHighlighted(
      shouldCellBeHighlighted(
        highlightCtx.highlightInfo,
        cellState
      )
    )
  }, [highlightCtx.highlightInfo]);

  function handleShiftSelection(e: ContextMenuItemSelectedEvent) {
    const shift = getWorkShift(e.detail.value.text as string)
    setCellState(old => ({ ...old, shift: shift }))
  }

  function handleLeftClick() {
    if (!highlightCtx.highlightInfo) {
      highlightCtx.startHighlight(cellState);
    } else {
      highlightCtx.stopHighlight()
    }
    props.onCellLeftClick?.(cellState)
  }

  function handleMouseEnter() {
    if (highlightCtx.highlightInfo) {
      highlightCtx.setCurrentHoverCell(cellState)
    }
  }

  return (
    <ContextMenu
      items={cellContextMenuItems}
      onItemSelected={(e) => handleShiftSelection(e)}
    >
      <div
        key={cellState.index + cellState.owner}
        style={{
          borderRadius: "var(--lumo-border-radius-m)",
          alignItems: "center",
          justifyContent: "center",
          display: "flex",
          minHeight: 40,
          backgroundColor: isHighlighted ? "var(--lumo-success-color-50pct)" : undefined,
          userSelect: "none"
        }}
        onClick={handleLeftClick}
        onMouseEnter={handleMouseEnter}
      >
        <span style={{ fontSize: 30 }}>
          {workShiftBindings[cellState.shift].symbol}
        </span>
      </div>
    </ContextMenu>
  );
}
