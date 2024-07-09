import { dateToString, generateUUID } from "Frontend/util/utils";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintType";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";
import ShiftPatternRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftPatternRequestDTO";
import TripleShiftConstraintRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/TripleShiftConstraintRequestDTO";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ConsecutiveWorkingDaysRequestDTO";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftFollowupRestrictionRequestDTO";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftsPerScheduleRequestDTO";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeesPerShiftRequestDTO";
import EmployeeShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeeShiftRequestDTO";
import TeamAssignmentRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/TeamAssignmentRequestDTO";

type ConstraintBinding<T> = {
  label: string
  constraint: T
}

type DefaultConstraints = {
  [ConstraintType.SHIFT_PATTERN_CONSTRAINT]: ConstraintBinding<ShiftPatternRequestDTO>
  [ConstraintType.EMPLOYEE_SHIFT_REQUEST]: ConstraintBinding<EmployeeShiftRequestDTO>
  [ConstraintType.TRIPLE_SHIFTS_CONSTRAINT]: ConstraintBinding<TripleShiftConstraintRequestDTO>
  [ConstraintType.CONSECUTIVE_WORKING_DAYS]: ConstraintBinding<ConsecutiveWorkingDaysRequestDTO>
  [ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION]: ConstraintBinding<ShiftFollowupRestrictionRequestDTO>
  [ConstraintType.SHIFT_PER_SCHEDULE]: ConstraintBinding<ShiftsPerScheduleRequestDTO>
  [ConstraintType.EMPLOYEES_PER_SHIFT]: ConstraintBinding<EmployeesPerShiftRequestDTO>
  [ConstraintType.TEAM_ASSIGNMENT]: ConstraintBinding<TeamAssignmentRequestDTO>
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
      owner: { id: 0 },
      shiftPattern: [],
      reward: 1,
      startDayIndex: 0
    }
  },
  [ConstraintType.EMPLOYEE_SHIFT_REQUEST]: {
    label: "Specifická směna na dané datum",
    constraint: {
      type: ConstraintType.EMPLOYEE_SHIFT_REQUEST,
      id: generateUUID(),
      owner: { id: 0 },
      date: dateToString(new Date()),
      requestedShift: WorkShifts.ANY
    }
  },
  [ConstraintType.SHIFT_PER_SCHEDULE]: {
    label: "Počet směn v rozvrhu",
    constraint: {
      id: generateUUID(),
      owner: { id: 0 },
      type: ConstraintType.SHIFT_PER_SCHEDULE,
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
      owner: { id: 0 },
      penaltyForShiftTripletOutsideWeekend: 50
    }
  },
  [ConstraintType.TEAM_ASSIGNMENT]: {
    label: "Přiřazení do týmu",
    constraint: {
      type: ConstraintType.TEAM_ASSIGNMENT,
      id: generateUUID(),
      owner: { id: 0 },
      isLeader: false,
      teamId: 1,
      penalty: 50
    }
  }
}