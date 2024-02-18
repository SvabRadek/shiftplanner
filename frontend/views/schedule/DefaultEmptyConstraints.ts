import ConstraintType from "Frontend/generated/com/cocroachden/planner/lib/ConstraintType";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { dateToStupidDate } from "Frontend/util/utils";


export type Constraint = {
  type: ConstraintType
  [x: string]: unknown
}

type ConstraintBinding = {
  type: ConstraintType
  constraint: Constraint
  label: string
}
type DefaultConstraints = Record<ConstraintType, ConstraintBinding>

export const apolinarPattern: WorkShifts[] = [
  WorkShifts.DAY,
  WorkShifts.DAY,
  WorkShifts.NIGHT,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.DAY,
  WorkShifts.NIGHT,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.DAY,
  WorkShifts.NIGHT,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.DAY,
  WorkShifts.NIGHT,
  WorkShifts.NIGHT,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.DAY,
  WorkShifts.NIGHT,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.DAY,
  WorkShifts.NIGHT,
  WorkShifts.OFF,
  WorkShifts.OFF
]

export const classicPattern: WorkShifts[] = [
  WorkShifts.DAY,
  WorkShifts.DAY,
  WorkShifts.DAY,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.NIGHT,
  WorkShifts.NIGHT,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.DAY,
  WorkShifts.DAY,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.NIGHT,
  WorkShifts.NIGHT,
  WorkShifts.NIGHT,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.DAY,
  WorkShifts.DAY,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.OFF,
  WorkShifts.NIGHT,
  WorkShifts.NIGHT,
  WorkShifts.OFF,
  WorkShifts.OFF
]
export const defaultConstraints: DefaultConstraints = {
  [ConstraintType.CONSECUTIVE_WORKING_DAYS]: {
    type: ConstraintType.CONSECUTIVE_WORKING_DAYS,
    label: "Pocet po sobe jdoucich smen",
    constraint: {
      type: ConstraintType.CONSECUTIVE_WORKING_DAYS,
      hardMin: 0,
      softMin: 0,
      minPenalty: 1,
      softMax: 0,
      hardMax: 0,
      maxPenalty: 1
    }
  },
  [ConstraintType.WORKERS_PER_SHIFT]: {
    type: ConstraintType.WORKERS_PER_SHIFT,
    label: "Pocet pracovniku na smenu",
    constraint: {
      type: ConstraintType.WORKERS_PER_SHIFT,
      targetShift: WorkShifts.DAY,
      softMin: 0,
      hardMin: 0,
      minPenalty: 1,
      softMax: 0,
      hardMax: 0,
      maxPenalty: 1
    }
  },
  [ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION]: {
    type: ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION,
    label: "Omezeni navaznosti smen",
    constraint: {
      type: ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION,
      firstShift: WorkShifts.NIGHT,
      forbiddenFollowup: WorkShifts.DAY,
      penalty: 0
    }
  },
  [ConstraintType.ONE_SHIFT_PER_DAY]: {
    type: ConstraintType.ONE_SHIFT_PER_DAY,
    label: "Jedna smena za den",
    constraint: {
      type: ConstraintType.ONE_SHIFT_PER_DAY,
    }
  },
  [ConstraintType.SHIFT_PATTERN_POSITIVE_CONSTRAINT]: {
    type: ConstraintType.SHIFT_PATTERN_POSITIVE_CONSTRAINT,
    label: "Vzor rozlozeni smen",
    constraint: {
      type: ConstraintType.SHIFT_PATTERN_POSITIVE_CONSTRAINT,
      shiftPattern: [],
      reward: 1,
      startDayIndex: 0
    }
  },
  [ConstraintType.SPECIFIC_SHIFT_REQUEST]: {
    type: ConstraintType.SPECIFIC_SHIFT_REQUEST,
    label: "Specificka smena na dane datum",
    constraint: {
      type: ConstraintType.SPECIFIC_SHIFT_REQUEST,
      owner: "none",
      date: dateToStupidDate(new Date()),
      requestedShift: WorkShifts.ANY
    }
  },
  [ConstraintType.SHIFT_PER_SCHEDULE]: {
    type: ConstraintType.SHIFT_PER_SCHEDULE,
    label: "Pocet smen v rozvrhu",
    constraint: {
      owner: "none",
      type: ConstraintType.SHIFT_PER_SCHEDULE,
      targetShift: WorkShifts.ANY,
      hardMin: 0,
      softMin: 0,
      minPenalty: 1,
      softMax: 0,
      maxPenalty: 1,
      hardMax: 0
    }
  }
}