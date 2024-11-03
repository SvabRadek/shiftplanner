import ConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintDTO";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintType";
import ShiftPatternConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ShiftPatternConstraintDTO";
import RequestedShiftConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/RequestedShiftConstraintDTO";
import TripleShiftConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/TripleShiftConstraintDTO";
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

export type SortedConstraints = {
    [ConstraintType.SHIFT_PATTERN_CONSTRAINT]: ShiftPatternConstraintDTO[]
    [ConstraintType.REQUESTED_SHIFT_CONSTRAINT]: RequestedShiftConstraintDTO[]
    [ConstraintType.TRIPLE_SHIFTS_CONSTRAINT]: TripleShiftConstraintDTO[]
    [ConstraintType.CONSECUTIVE_WORKING_DAYS]: ConsecutiveWorkingDaysConstraintDTO[]
    [ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION]: ShiftFollowupRestrictionConstraintDTO[]
    [ConstraintType.SHIFTS_PER_SCHEDULE]: ShiftsPerScheduleConstraintDTO[]
    [ConstraintType.EMPLOYEES_PER_SHIFT]: EmployeesPerShiftConstraintDTO[]
    [ConstraintType.TEAM_ASSIGNMENT]: TeamAssignmentConstraintDTO[]
    [ConstraintType.WEEKEND_CONSTRAINT]: WeekendConstraintDTO[]
    [ConstraintType.EVEN_SHIFT_DISTRIBUTION]: EvenShiftDistributionConstraintDTO[]
    [ConstraintType.ONE_SHIFT_PER_DAY]: ConstraintDTO[]
}

export function sortConstraints(constraints: ConstraintDTO[]): SortedConstraints {
    const filter = (type: ConstraintType) => constraints.filter(c => c.type === type)
    return {
        [ConstraintType.REQUESTED_SHIFT_CONSTRAINT]: filter(ConstraintType.REQUESTED_SHIFT_CONSTRAINT) as RequestedShiftConstraintDTO[],
        [ConstraintType.SHIFTS_PER_SCHEDULE]: filter(ConstraintType.SHIFTS_PER_SCHEDULE) as ShiftsPerScheduleConstraintDTO[],
        [ConstraintType.CONSECUTIVE_WORKING_DAYS]: filter(ConstraintType.CONSECUTIVE_WORKING_DAYS) as ConsecutiveWorkingDaysConstraintDTO[],
        [ConstraintType.EMPLOYEES_PER_SHIFT]: filter(ConstraintType.EMPLOYEES_PER_SHIFT) as EmployeesPerShiftConstraintDTO[],
        [ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION]: filter(ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION) as ShiftFollowupRestrictionConstraintDTO[],
        [ConstraintType.SHIFT_PATTERN_CONSTRAINT]: filter(ConstraintType.SHIFT_PATTERN_CONSTRAINT) as ShiftPatternConstraintDTO[],
        [ConstraintType.TRIPLE_SHIFTS_CONSTRAINT]: filter(ConstraintType.TRIPLE_SHIFTS_CONSTRAINT) as TripleShiftConstraintDTO[],
        [ConstraintType.TEAM_ASSIGNMENT]: filter(ConstraintType.TEAM_ASSIGNMENT) as TeamAssignmentConstraintDTO[],
        [ConstraintType.WEEKEND_CONSTRAINT]: filter(ConstraintType.WEEKEND_CONSTRAINT) as WeekendConstraintDTO[],
        [ConstraintType.EVEN_SHIFT_DISTRIBUTION]: filter(ConstraintType.EVEN_SHIFT_DISTRIBUTION) as EvenShiftDistributionConstraintDTO[],
        [ConstraintType.ONE_SHIFT_PER_DAY]: []
    }
}

export function sortedConstraintsForEmployee(employeeId: string, sortedConstraints: SortedConstraints): SortedConstraints {
    return {
        [ConstraintType.REQUESTED_SHIFT_CONSTRAINT]: sortedConstraints[ConstraintType.REQUESTED_SHIFT_CONSTRAINT].filter(c => c.owner === employeeId),
        [ConstraintType.SHIFTS_PER_SCHEDULE]: sortedConstraints[ConstraintType.SHIFTS_PER_SCHEDULE].filter(c => c.owner === employeeId),
        [ConstraintType.CONSECUTIVE_WORKING_DAYS]: sortedConstraints[ConstraintType.CONSECUTIVE_WORKING_DAYS].filter(c => c.owner === employeeId),
        [ConstraintType.EMPLOYEES_PER_SHIFT]: sortedConstraints[ConstraintType.EMPLOYEES_PER_SHIFT],
        [ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION]: sortedConstraints[ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION].filter(c => c.owner === employeeId),
        [ConstraintType.SHIFT_PATTERN_CONSTRAINT]: sortedConstraints[ConstraintType.SHIFT_PATTERN_CONSTRAINT].filter(c => c.owner === employeeId),
        [ConstraintType.TRIPLE_SHIFTS_CONSTRAINT]: sortedConstraints[ConstraintType.TRIPLE_SHIFTS_CONSTRAINT].filter(c => c.owner === employeeId),
        [ConstraintType.TEAM_ASSIGNMENT]: sortedConstraints[ConstraintType.TEAM_ASSIGNMENT].filter(c => c.owner === employeeId),
        [ConstraintType.WEEKEND_CONSTRAINT]: sortedConstraints[ConstraintType.WEEKEND_CONSTRAINT].filter(c => c.owner === employeeId),
        [ConstraintType.EVEN_SHIFT_DISTRIBUTION]: sortedConstraints[ConstraintType.EVEN_SHIFT_DISTRIBUTION].filter(c => c.owner === employeeId),
        [ConstraintType.ONE_SHIFT_PER_DAY]: []
    }
}

export function mergeConstraintsIntoList(sortedConstraints: SortedConstraints): ConstraintDTO[] {
    return [
        ...sortedConstraints[ConstraintType.REQUESTED_SHIFT_CONSTRAINT],
        ...sortedConstraints[ConstraintType.SHIFTS_PER_SCHEDULE],
        ...sortedConstraints[ConstraintType.CONSECUTIVE_WORKING_DAYS],
        ...sortedConstraints[ConstraintType.EMPLOYEES_PER_SHIFT],
        ...sortedConstraints[ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION],
        ...sortedConstraints[ConstraintType.SHIFT_PATTERN_CONSTRAINT],
        ...sortedConstraints[ConstraintType.TRIPLE_SHIFTS_CONSTRAINT],
        ...sortedConstraints[ConstraintType.TEAM_ASSIGNMENT],
        ...sortedConstraints[ConstraintType.WEEKEND_CONSTRAINT],
        ...sortedConstraints[ConstraintType.EVEN_SHIFT_DISTRIBUTION],
    ]
}

export const defaultEmptySortedConstraints: SortedConstraints = {
    [ConstraintType.SHIFT_PATTERN_CONSTRAINT]: [],
    [ConstraintType.REQUESTED_SHIFT_CONSTRAINT]: [],
    [ConstraintType.TRIPLE_SHIFTS_CONSTRAINT]: [],
    [ConstraintType.CONSECUTIVE_WORKING_DAYS]: [],
    [ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION]: [],
    [ConstraintType.SHIFTS_PER_SCHEDULE]: [],
    [ConstraintType.EMPLOYEES_PER_SHIFT]: [],
    [ConstraintType.TEAM_ASSIGNMENT]: [],
    [ConstraintType.WEEKEND_CONSTRAINT]: [],
    [ConstraintType.EVEN_SHIFT_DISTRIBUTION]: [],
    [ConstraintType.ONE_SHIFT_PER_DAY]: []
}
