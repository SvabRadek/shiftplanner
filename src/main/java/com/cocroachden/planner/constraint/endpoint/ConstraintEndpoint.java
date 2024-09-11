package com.cocroachden.planner.constraint.endpoint;

import com.cocroachden.planner.constraint.ConstraintId;
import com.cocroachden.planner.constraint.api.*;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.constraint.ConstraintMapper;
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


  public @Nonnull List<@Nonnull ConstraintRequestDTO> findRequests(@Nonnull List<@Nonnull String> requestIds) {
    return this.getRecords(requestIds).stream()
        .map(ConstraintMapper::fromRecord)
        .filter(Objects::nonNull)
        .toList();
  }

  public @Nonnull List<@Nonnull EmployeeShiftRequestDTO> findSpecificShiftRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, EmployeeShiftRequestDTO.class))
        .toList();
  }

  public @Nonnull List<@Nonnull ShiftsPerScheduleRequestDTO> findShiftsPerScheduleRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, ShiftsPerScheduleRequestDTO.class))
        .toList();
  }

  public @Nonnull List<@Nonnull ConsecutiveWorkingDaysRequestDTO> findConsecutiveWorkingDaysRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, ConsecutiveWorkingDaysRequestDTO.class))
        .toList();
  }

  public @Nonnull List<@Nonnull EmployeesPerShiftRequestDTO> findEmployeesPerShiftRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, EmployeesPerShiftRequestDTO.class))
        .toList();
  }

  public @Nonnull List<@Nonnull ShiftFollowupRestrictionRequestDTO> findShiftFollowupRestrictionRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, ShiftFollowupRestrictionRequestDTO.class))
        .toList();
  }

  public @Nonnull List<@Nonnull ShiftPatternRequestDTO> findShiftPatternRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, ShiftPatternRequestDTO.class))
        .toList();
  }

  public @Nonnull List<@Nonnull TripleShiftConstraintRequestDTO> findTripleShiftConstraintRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, TripleShiftConstraintRequestDTO.class))
        .toList();
  }

  public @Nonnull List<@Nonnull TeamAssignmentRequestDTO> findTeamAssignmentsConstraintRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, TeamAssignmentRequestDTO.class))
        .toList();
  }

  public @Nonnull List<@Nonnull WeekendRequestDTO> findWeekendConstraintRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, WeekendRequestDTO.class))
        .toList();
  }

  public @Nonnull List<@Nonnull EvenShiftDistributionRequestDTO> findEvenShiftDistributionRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> ConstraintMapper.specificFromRecord(r, EvenShiftDistributionRequestDTO.class))
        .toList();
  }

  public List<ConstraintRequestRecord> getRecords(List<String> constraintIds) {
    var ids = constraintIds.stream().map(ConstraintId::new).toList();
    return StreamSupport.stream(
        constraintRequestRepository.findAllById(ids).spliterator(),
        false
    ).toList();
  }
}
