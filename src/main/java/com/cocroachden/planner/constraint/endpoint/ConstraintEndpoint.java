package com.cocroachden.planner.constraint.endpoint;

import com.cocroachden.planner.constraint.api.*;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.EmployeeShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.teamassignment.request.TeamAssignmentRequest;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraintRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class ConstraintEndpoint {
  private final ConstraintRequestRepository constraintRequestRepository;


  public @Nonnull List<@Nonnull ConstraintRequestDTO> findRequests(@Nonnull List<@Nonnull UUID> requestIds) {
    return this.getRecords(requestIds).stream()
        .map(this::convertToDto)
        .filter(Objects::nonNull)
        .toList();
  }

  public @Nonnull List<@Nonnull EmployeeShiftRequestDTO> findSpecificShiftRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> EmployeeShiftRequestDTO.from(r.getId(), (EmployeeShiftRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull ShiftsPerScheduleRequestDTO> findShiftsPerScheduleRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ShiftsPerScheduleRequestDTO.from(r.getId(), (ShiftsPerScheduleRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull ConsecutiveWorkingDaysRequestDTO> findConsecutiveWorkingDaysRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConsecutiveWorkingDaysRequestDTO.from(r.getId(), (ConsecutiveWorkingDaysRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull EmployeesPerShiftRequestDTO> findEmployeesPerShiftRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> EmployeesPerShiftRequestDTO.from(r.getId(), (EmployeesPerShiftRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull ShiftFollowupRestrictionRequestDTO> findShiftFollowupRestrictionRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ShiftFollowupRestrictionRequestDTO.from(r.getId(), (ShiftFollowUpRestrictionRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull ShiftPatternRequestDTO> findShiftPatternRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ShiftPatternRequestDTO.from(r.getId(), (ShiftPatternConstraintRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull TripleShiftConstraintRequestDTO> findTripleShiftConstraintRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> TripleShiftConstraintRequestDTO.from(r.getId(), (TripleShiftConstraintRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull TeamAssignmentRequestDTO> findTeamAssignmentsConstraintRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> TeamAssignmentRequestDTO.from(r.getId(), (TeamAssignmentRequest) r.getRequest()))
        .toList();
  }

  public List<ConstraintRequestRecord> getRecords(List<UUID> constraintIds) {
    return StreamSupport.stream(
        constraintRequestRepository.findAllById(constraintIds).spliterator(),
        false
    ).toList();
  }

  private ConstraintRequestDTO convertToDto(ConstraintRequestRecord record) {
    return switch (record.getType()) {
      case EMPLOYEE_SHIFT_REQUEST -> EmployeeShiftRequestDTO.from(
          record.getId(),
          (EmployeeShiftRequest) record.getRequest()
      );
      case SHIFT_PER_SCHEDULE -> ShiftsPerScheduleRequestDTO.from(
          record.getId(),
          (ShiftsPerScheduleRequest) record.getRequest()
      );
      case CONSECUTIVE_WORKING_DAYS -> ConsecutiveWorkingDaysRequestDTO.from(
          record.getId(),
          (ConsecutiveWorkingDaysRequest) record.getRequest()
      );
      case SHIFT_FOLLOW_UP_RESTRICTION -> ShiftFollowupRestrictionRequestDTO.from(
          record.getId(),
          (ShiftFollowUpRestrictionRequest) record.getRequest()
      );
      case SHIFT_PATTERN_CONSTRAINT -> ShiftPatternRequestDTO.from(
          record.getId(),
          (ShiftPatternConstraintRequest) record.getRequest()
      );
      case EMPLOYEES_PER_SHIFT -> EmployeesPerShiftRequestDTO.from(
          record.getId(),
          (EmployeesPerShiftRequest) record.getRequest()
      );
      case TRIPLE_SHIFTS_CONSTRAINT -> TripleShiftConstraintRequestDTO.from(
          record.getId(),
          (TripleShiftConstraintRequest) record.getRequest()
      );
      case TEAM_ASSIGNMENT -> TeamAssignmentRequestDTO.from(
          record.getId(),
          (TeamAssignmentRequest) record.getRequest()
      );
      case ONE_SHIFT_PER_DAY -> null;
    };
  }
}
