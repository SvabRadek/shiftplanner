package com.cocroachden.planner.solver.api;

import com.cocroachden.planner.constraint.api.*;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.request.WorkersPerShiftRequest;

public class ConstraintMapper {

  public static ConstraintRequestDTO fromRecord(ConstraintRequestRecord record) {
    var request = record.getRequest();
    if (request instanceof SpecificShiftRequest specificShiftRequest) {
      return SpecificShiftRequestDTO.from(record.getId(), specificShiftRequest);
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
    if (request instanceof WorkersPerShiftRequest workersPerShiftRequest) {
      return EmployeesPerShiftRequestDTO.from(record.getId(), workersPerShiftRequest);
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
    if (dto instanceof SpecificShiftRequestDTO specificShiftRequest) {
      return SpecificShiftRequest.from(specificShiftRequest);
    }
    if (dto instanceof ShiftFollowupRestrictionRequestDTO followUpRestrictionRequest) {
      return ShiftFollowUpRestrictionRequest.from(followUpRestrictionRequest);
    }
    if (dto instanceof ShiftPatternRequestDTO shiftPatternConstraintRequest) {
      return ShiftPatternConstraintRequest.from(shiftPatternConstraintRequest);
    }
    if (dto instanceof ShiftsPerScheduleRequestDTO shiftsPerScheduleRequest) {
      return ShiftsPerScheduleRequest.from(shiftsPerScheduleRequest);
    }
    if (dto instanceof EmployeesPerShiftRequestDTO workersPerShiftRequest) {
      return WorkersPerShiftRequest.from(workersPerShiftRequest);
    }
    if (dto instanceof ConsecutiveWorkingDaysRequestDTO consecutiveWorkingDaysRequest) {
      return ConsecutiveWorkingDaysRequest.from(consecutiveWorkingDaysRequest);
    }
    if (dto instanceof TripleShiftConstraintRequestDTO tripleShiftConstraintRequest) {
      return TripleShiftConstraintRequest.from(tripleShiftConstraintRequest);
    }
    throw new IllegalArgumentException("Cannot remap requested type: " + dto.getType());
  }
}
