import { createContext, FC, ReactNode, useEffect, useState } from "react";
import { Cell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";

type ContextType = {
  originShift: WorkShifts
  originCell?: Cell
  currentHoverCell?: Cell
  highlightInfo?: HighlightInfo
  startHighlight: (cell: Cell | undefined) => void
  stopHighlight: () => void
  setCurrentHoverCell: (cell: Cell | undefined) => void
}

export type HighlightInfo = {
  owner: string
  indexes: number[]
}

export const HighlightContext = createContext<ContextType>({
  originShift: WorkShifts.ANY,
  stopHighlight: () => {},
  startHighlight: cell => {},
  setCurrentHoverCell: cell => {},
});
export const HighlightCxtProvider: FC<{ children?: ReactNode | undefined }> = props => {

  const [originShift, setOriginShift] = useState<WorkShifts>(WorkShifts.ANY)
  const [originCell, setOriginCell] = useState<Cell | undefined>(undefined)
  const [currentHoverCell, setCurrentHoverCell] = useState<Cell | undefined>(undefined)
  const [highlightInfo, setHighlightInfo] = useState<HighlightInfo | undefined>(undefined)

  useEffect(() => {
    if (!originCell) {
      setHighlightInfo(() => undefined)
      return
    }
    setOriginShift(() => originCell.shift)
    if (!currentHoverCell) {
      setHighlightInfo(() => undefined)
      return
    }
    setHighlightInfo(() => ({
     owner: originCell?.owner,
     indexes: calculateHighlightedIndexes(originCell, currentHoverCell)
    }));
  }, [originCell, currentHoverCell]);

  return (
    <HighlightContext.Provider value={{
      originShift,
      originCell,
      currentHoverCell,
      highlightInfo,
      stopHighlight: () => {
        setOriginCell(() => undefined)
        setCurrentHoverCell(() => undefined)
      },
      startHighlight: cell => {
        setOriginCell(cell)
        setCurrentHoverCell(cell)
      },
      setCurrentHoverCell: cell => setCurrentHoverCell(cell)
    }}>
      {props.children}
    </HighlightContext.Provider>
  );
};

function calculateHighlightedIndexes(
  originCell: Cell | undefined,
  currentHoverCell: Cell | undefined
): number[] {
  const indexes: number[] = []
  if(!originCell) return indexes
  if(!currentHoverCell) return indexes
  if(originCell.index === currentHoverCell.index) return [originCell.index]
  if (originCell.index < currentHoverCell.index) {
    for (let i = originCell.index; i <= currentHoverCell.index; i++) {
      indexes.push(i)
    }
    return indexes;
  }
  if (originCell.index > currentHoverCell.index) {
    for (let i = currentHoverCell.index; i <= originCell.index; i++) {
      indexes.push(i)
    }
    return indexes
  }
  return []
}

