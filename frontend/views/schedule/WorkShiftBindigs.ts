import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";

export type WorkShiftBinding = {
  shift: WorkShifts,
  symbol: string,
  fullText: string
  indexOfNext: number
}

export const workShiftBindings: Record<string, WorkShiftBinding> = {
  ANY: { shift: WorkShifts.ANY, symbol: " ", fullText: "Cokoliv", indexOfNext: 1 },
  DAY: { shift: WorkShifts.DAY, symbol: "D", fullText: "Denni", indexOfNext: 2 },
  NIGHT: { shift: WorkShifts.NIGHT, symbol: "N", fullText: "Nocni", indexOfNext: 3 },
  OFF: { shift: WorkShifts.OFF, symbol: "V", fullText: "Volno", indexOfNext: 4 },
  NOT_DAY: { shift: WorkShifts.NOT_DAY, symbol: "!D", fullText: "Ne denni", indexOfNext: 5 },
  NOT_NIGHT: { shift: WorkShifts.NOT_NIGHT, symbol: "!N", fullText: "Ne nocni", indexOfNext: 6 },
  WORKING_SHIFTS: { shift: WorkShifts.WORKING_SHIFTS, symbol: "P", fullText: "Pracovni", indexOfNext: 0 },
}
