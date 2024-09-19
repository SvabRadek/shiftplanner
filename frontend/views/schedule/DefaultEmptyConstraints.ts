import {dateToString, generateUUID} from "Frontend/util/utils";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintType";
import ShiftPatternConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ShiftPatternConstraintDTO";
import RequestedShiftConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/RequestedShiftConstraintDTO";
import TripleShiftConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/TripleShiftConstraintDTO";
import ConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintDTO";
import ConsecutiveWorkingDaysConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ConsecutiveWorkingDaysConstraintDTO";
import ShiftFollowupRestrictionConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionConstraintDTO";
import ShiftsPerScheduleConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleConstraintDTO";
import EmployeesPerShiftConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftConstraintDTO";
import TeamAssignmentConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/TeamAssignmentConstraintDTO";
import WeekendConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/WeekendConstraintDTO";
import EvenShiftDistributionConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EvenShiftDistributionConstraintDTO";

type ConstraintBinding<T> = {
  label: string
  constraint: T
}

type DefaultConstraints = {
  [ConstraintType.SHIFT_PATTERN_CONSTRAINT]: ConstraintBinding<ShiftPatternConstraintDTO>
  [ConstraintType.REQUESTED_SHIFT_CONSTRAINT]: ConstraintBinding<RequestedShiftConstraintDTO>
  [ConstraintType.TRIPLE_SHIFTS_CONSTRAINT]: ConstraintBinding<TripleShiftConstraintDTO>
  [ConstraintType.CONSECUTIVE_WORKING_DAYS]: ConstraintBinding<ConsecutiveWorkingDaysConstraintDTO>
  [ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION]: ConstraintBinding<ShiftFollowupRestrictionConstraintDTO>
  [ConstraintType.SHIFTS_PER_SCHEDULE]: ConstraintBinding<ShiftsPerScheduleConstraintDTO>
  [ConstraintType.EMPLOYEES_PER_SHIFT]: ConstraintBinding<EmployeesPerShiftConstraintDTO>
  [ConstraintType.TEAM_ASSIGNMENT]: ConstraintBinding<TeamAssignmentConstraintDTO>
  [ConstraintType.WEEKEND_CONSTRAINT]: ConstraintBinding<WeekendConstraintDTO>
  [ConstraintType.EVEN_SHIFT_DISTRIBUTION]: ConstraintBinding<EvenShiftDistributionConstraintDTO>
}

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
    label: "Počet po sobě jdoucích směn",
    constraint: {
      type: ConstraintType.CONSECUTIVE_WORKING_DAYS,
      id: generateUUID(),
      owner: "default",
      targetShift: WorkShifts.WORKING_SHIFTS,
      hardMin: 0,
      softMin: 0,
      minPenalty: 1,
      softMax: 0,
      hardMax: 0,
      maxPenalty: 1
    }
  },
  [ConstraintType.EMPLOYEES_PER_SHIFT]: {
    label: "Počet pracovníků na směnu",
    constraint: {
      type: ConstraintType.EMPLOYEES_PER_SHIFT,
      id: generateUUID(),
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
    label: "Omezení návaznosti směn",
    constraint: {
      type: ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION,
      id: generateUUID(),
      owner: "default",
      firstShift: WorkShifts.NIGHT,
      forbiddenFollowup: WorkShifts.DAY,
      penalty: 0
    }
  },
  [ConstraintType.SHIFT_PATTERN_CONSTRAINT]: {
    label: "Vzor rozložení směn",
    constraint: {
      type: ConstraintType.SHIFT_PATTERN_CONSTRAINT,
      id: generateUUID(),
      owner: "default",
      shiftPattern: [],
      reward: 1,
      startDayIndex: 0
    }
  },
  [ConstraintType.REQUESTED_SHIFT_CONSTRAINT]: {
    label: "Specifická směna na dané datum",
    constraint: {
      type: ConstraintType.REQUESTED_SHIFT_CONSTRAINT,
      id: generateUUID(),
      owner: "default",
      date: dateToString(new Date()),
      requestedShift: WorkShifts.ANY
    }
  },
  [ConstraintType.SHIFTS_PER_SCHEDULE]: {
    label: "Počet směn v rozvrhu",
    constraint: {
      id: generateUUID(),
      owner: "default",
      type: ConstraintType.SHIFTS_PER_SCHEDULE,
      targetShift: WorkShifts.ANY,
      hardMin: 0,
      softMin: 0,
      minPenalty: 1,
      softMax: 0,
      maxPenalty: 1,
      hardMax: 0
    }
  },
  [ConstraintType.TRIPLE_SHIFTS_CONSTRAINT]: {
    label: "Nastavení trojitých směn",
    constraint: {
      type: ConstraintType.TRIPLE_SHIFTS_CONSTRAINT,
      id: generateUUID(),
      owner: "default",
      penaltyForShiftTripletOutsideWeekend: 50,
      areAllowed: true
    }
  },
  [ConstraintType.TEAM_ASSIGNMENT]: {
    label: "Přiřazení do týmu",
    constraint: {
      type: ConstraintType.TEAM_ASSIGNMENT,
      id: generateUUID(),
      owner: "default",
      isLeader: false,
      teamId: 1,
      penalty: 50
    }
  },
  [ConstraintType.WEEKEND_CONSTRAINT]: {
    label: "Nastavení víkendu",
    constraint: {
      type: ConstraintType.WEEKEND_CONSTRAINT,
      id: generateUUID(),
      owner: "default",
      assignOnlyFullWorkingWeekends: true,
      penaltyForNotFullWorkingWeekend: 50
    }
  },
  [ConstraintType.EVEN_SHIFT_DISTRIBUTION]: {
    label: "Rozložení směn",
    constraint: {
      type: ConstraintType.EVEN_SHIFT_DISTRIBUTION,
      id: generateUUID(),
      owner: "default",
      distributeShiftsEvenlyThroughoutSchedule: true,
      penaltyForDeviationFromWeeksAverage: 10
    }
  }
}