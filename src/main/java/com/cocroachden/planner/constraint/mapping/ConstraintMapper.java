package com.cocroachden.planner.constraint.mapping;

import com.cocroachden.planner.constraint.*;
import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.EmployeeShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request.EvenShiftDistributionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.teamassignment.request.TeamAssignmentRequest;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.weekends.request.WeekendRequest;

public class ConstraintMapper {

    public static ConstraintRequestDTO fromRecord(ConstraintRecord record) {
        var request = record.getRequest();
        switch (record.getType()) {
            case EMPLOYEE_SHIFT_REQUEST -> {
                return EmployeeShiftRequestDTO.from(record.getId().getId(), (EmployeeShiftRequest) record.getRequest());
            }
            case SHIFT_PER_SCHEDULE -> {
                return ShiftsPerScheduleRequestDTO.from(record.getId().getId(), (ShiftsPerScheduleRequest) record.getRequest());
            }
            case CONSECUTIVE_WORKING_DAYS -> {
                return ConsecutiveWorkingDaysRequestDTO.from(
                        record.getId().getId(),
                        (ConsecutiveWorkingDaysRequest) record.getRequest()
                );
            }
            case ONE_SHIFT_PER_DAY -> {
                return null;
            }
            case SHIFT_FOLLOW_UP_RESTRICTION -> {
                return ShiftFollowupRestrictionRequestDTO.from(
                        record.getId().getId(),
                        (ShiftFollowUpRestrictionRequest) record.getRequest()
                );
            }
            case SHIFT_PATTERN_CONSTRAINT -> {
                return ShiftPatternRequestDTO.from(record.getId().getId(), (ShiftPatternConstraintRequest) record.getRequest());
            }
            case EMPLOYEES_PER_SHIFT -> {
                return EmployeesPerShiftRequestDTO.from(record.getId().getId(), (EmployeesPerShiftRequest) record.getRequest());
            }
            case TRIPLE_SHIFTS_CONSTRAINT -> {
                return TripleShiftConstraintRequestDTO.from(record.getId().getId(), (TripleShiftConstraintRequest) record.getRequest());
            }
            case TEAM_ASSIGNMENT -> {
                return TeamAssignmentRequestDTO.from(record.getId().getId(), (TeamAssignmentRequest) record.getRequest());
            }
            case WEEKEND_REQUEST -> {
                return WeekendRequestDTO.from(record.getId().getId(), (WeekendRequest) record.getRequest());
            }
            case EVEN_SHIFT_DISTRIBUTION -> {
                return EvenShiftDistributionRequestDTO.from(record.getId().getId(), (EvenShiftDistributionRequest) record.getRequest());
            }
        }
        throw new IllegalArgumentException("Cannot remap requested type: " + request.getType());
    }

    public static <T extends ConstraintRequestDTO> T specificFromRecord(ConstraintRecord record, Class<T> clazz) {
        return clazz.cast(fromRecord(record));
    }

    public static ConstraintRequest fromDto(ConstraintRequestDTO dto) {
        switch (dto.getType()) {
            case EMPLOYEE_SHIFT_REQUEST -> {
                return EmployeeShiftRequest.from((EmployeeShiftRequestDTO) dto);
            }
            case SHIFT_PER_SCHEDULE -> {
                return ShiftsPerScheduleRequest.from((ShiftsPerScheduleRequestDTO) dto);
            }
            case CONSECUTIVE_WORKING_DAYS -> {
                return ConsecutiveWorkingDaysRequest.from((ConsecutiveWorkingDaysRequestDTO) dto);
            }
            case SHIFT_FOLLOW_UP_RESTRICTION -> {
                return ShiftFollowUpRestrictionRequest.from((ShiftFollowupRestrictionRequestDTO) dto);
            }
            case SHIFT_PATTERN_CONSTRAINT -> {
                return ShiftPatternConstraintRequest.from((ShiftPatternRequestDTO) dto);
            }
            case EMPLOYEES_PER_SHIFT -> {
                return EmployeesPerShiftRequest.from((EmployeesPerShiftRequestDTO) dto);
            }
            case TRIPLE_SHIFTS_CONSTRAINT -> {
                return TripleShiftConstraintRequest.from((TripleShiftConstraintRequestDTO) dto);
            }
            case TEAM_ASSIGNMENT -> {
                return TeamAssignmentRequest.from((TeamAssignmentRequestDTO) dto);
            }
            case WEEKEND_REQUEST -> {
                return WeekendRequest.from((WeekendRequestDTO) dto);
            }
            case EVEN_SHIFT_DISTRIBUTION -> {
                return EvenShiftDistributionRequest.from((EvenShiftDistributionRequestDTO) dto);
            }
            case ONE_SHIFT_PER_DAY -> throw new IllegalArgumentException("Should not be part of api exchange");
        }
        throw new IllegalArgumentException("Cannot remap requested type: " + dto.getType());
    }
}
