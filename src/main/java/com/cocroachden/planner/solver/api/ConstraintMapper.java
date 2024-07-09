package com.cocroachden.planner.solver.api;

import com.cocroachden.planner.constraint.api.*;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.EmployeeShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.teamassignment.request.TeamAssignmentRequest;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraintRequest;

public class ConstraintMapper {

  public static ConstraintRequestDTO fromRecord(ConstraintRequestRecord record) {
    var request = record.getRequest();
    if (request instanceof EmployeeShiftRequest employeeShiftRequest) {
      return EmployeeShiftRequestDTO.from(record.getId(), employeeShiftRequest);
    }
    if (request instanceof ShiftFollowUpRestrictionRequest followUpRestrictionRequest) {
      return ShiftFollowupRestrictionRequestDTO.from(record.getId(), followUpRestrictionRequest);
    }
    if (request instanceof ShiftPatternConstraintRequest shiftPatternConstraintRequest) {
      return ShiftPatternRequestDTO.from(record.getId(), shiftPatternConstraintRequest);
    }
    if (request instanceof ShiftsPerScheduleRequest shiftsPerScheduleRequest) {
      return ShiftsPerScheduleRequestDTO.from(record.getId(), shiftsPerScheduleRequest);
    }
    if (request instanceof EmployeesPerShiftRequest employeesPerShiftRequest) {
      return EmployeesPerShiftRequestDTO.from(record.getId(), employeesPerShiftRequest);
    }
    if (request instanceof ConsecutiveWorkingDaysRequest consecutiveWorkingDaysRequest) {
      return ConsecutiveWorkingDaysRequestDTO.from(record.getId(), consecutiveWorkingDaysRequest);
    }
    if (request instanceof TripleShiftConstraintRequest tripleShiftConstraintRequest) {
      return TripleShiftConstraintRequestDTO.from(record.getId(), tripleShiftConstraintRequest);
    }
    throw new IllegalArgumentException("Cannot remap requested type: " + request.getType());
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
      case ONE_SHIFT_PER_DAY -> throw new IllegalArgumentException("Should not be part of api exchange");
    }
    throw new IllegalArgumentException("Cannot remap requested type: " + dto.getType());
  }
}
