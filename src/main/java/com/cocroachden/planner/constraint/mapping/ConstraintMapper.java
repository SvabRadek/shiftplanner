
package com.cocroachden.planner.constraint.mapping;

import com.cocroachden.planner.constraint.*;
import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysConstraint;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.RequestedShiftConstraint;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftConstraint;
import com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request.EvenShiftDistributionConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleConstraint;
import com.cocroachden.planner.solver.constraints.specific.teamassignment.request.TeamAssignmentConstraint;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraint;
import com.cocroachden.planner.solver.constraints.specific.weekends.request.WeekendConstraint;

public class ConstraintMapper {

    public static ConstraintDTO fromRecord(ConstraintRecord record) {
        var request = record.getRequest();
        switch (record.getType()) {
            case REQUESTED_SHIFT_CONSTRAINT -> {
                return RequestedShiftConstraintDTO.from(record.getId(), (RequestedShiftConstraint) record.getRequest());
            }
            case SHIFTS_PER_SCHEDULE -> {
                return ShiftsPerScheduleConstraintDTO.from(record.getId(), (ShiftsPerScheduleConstraint) record.getRequest());
            }
            case CONSECUTIVE_WORKING_DAYS -> {
                return ConsecutiveWorkingDaysConstraintDTO.from(
                        record.getId(),
                        (ConsecutiveWorkingDaysConstraint) record.getRequest()
                );
            }
            case ONE_SHIFT_PER_DAY -> {
                return null;
            }
            case SHIFT_FOLLOW_UP_RESTRICTION -> {
                return ShiftFollowupRestrictionConstraintDTO.from(
                        record.getId(),
                        (ShiftFollowUpRestrictionConstraint) record.getRequest()
                );
            }
            case SHIFT_PATTERN_CONSTRAINT -> {
                return ShiftPatternConstraintDTO.from(record.getId(), (ShiftPatternConstraint) record.getRequest());
            }
            case EMPLOYEES_PER_SHIFT -> {
                return EmployeesPerShiftConstraintDTO.from(record.getId(), (EmployeesPerShiftConstraint) record.getRequest());
            }
            case TRIPLE_SHIFTS_CONSTRAINT -> {
                return TripleShiftConstraintDTO.from(record.getId(), (TripleShiftConstraint) record.getRequest());
            }
            case TEAM_ASSIGNMENT -> {
                return TeamAssignmentConstraintDTO.from(record.getId(), (TeamAssignmentConstraint) record.getRequest());
            }
            case WEEKEND_CONSTRAINT -> {
                return WeekendConstraintDTO.from(record.getId(), (WeekendConstraint) record.getRequest());
            }
            case EVEN_SHIFT_DISTRIBUTION -> {
                return EvenShiftDistributionConstraintDTO.from(record.getId(), (EvenShiftDistributionConstraint) record.getRequest());
            }
        }
        throw new IllegalArgumentException("Cannot remap requested type: " + request.getType());
    }

    public static <T extends ConstraintDTO> T specificFromRecord(ConstraintRecord record, Class<T> clazz) {
        return clazz.cast(fromRecord(record));
    }

    public static SolverConstraint fromDto(ConstraintDTO dto) {
        switch (dto.getType()) {
            case REQUESTED_SHIFT_CONSTRAINT -> {
                return RequestedShiftConstraint.from((RequestedShiftConstraintDTO) dto);
            }
            case SHIFTS_PER_SCHEDULE -> {
                return ShiftsPerScheduleConstraint.from((ShiftsPerScheduleConstraintDTO) dto);
            }
            case CONSECUTIVE_WORKING_DAYS -> {
                return ConsecutiveWorkingDaysConstraint.from((ConsecutiveWorkingDaysConstraintDTO) dto);
            }
            case SHIFT_FOLLOW_UP_RESTRICTION -> {
                return ShiftFollowUpRestrictionConstraint.from((ShiftFollowupRestrictionConstraintDTO) dto);
            }
            case SHIFT_PATTERN_CONSTRAINT -> {
                return ShiftPatternConstraint.from((ShiftPatternConstraintDTO) dto);
            }
            case EMPLOYEES_PER_SHIFT -> {
                return EmployeesPerShiftConstraint.from((EmployeesPerShiftConstraintDTO) dto);
            }
            case TRIPLE_SHIFTS_CONSTRAINT -> {
                return TripleShiftConstraint.from((TripleShiftConstraintDTO) dto);
            }
            case TEAM_ASSIGNMENT -> {
                return TeamAssignmentConstraint.from((TeamAssignmentConstraintDTO) dto);
            }
            case WEEKEND_CONSTRAINT -> {
                return WeekendConstraint.from((WeekendConstraintDTO) dto);
            }
            case EVEN_SHIFT_DISTRIBUTION -> {
                return EvenShiftDistributionConstraint.from((EvenShiftDistributionConstraintDTO) dto);
            }
            case ONE_SHIFT_PER_DAY -> throw new IllegalArgumentException("Should not be part of api exchange");
        }
        throw new IllegalArgumentException("Cannot remap requested type: " + dto.getType());
    }
}
