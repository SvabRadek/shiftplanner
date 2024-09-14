package com.cocroachden.planner.solverconfiguration.endpoint;

import com.cocroachden.planner.constraint.*;
import com.cocroachden.planner.constraint.query.ConstraintQuery;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class ConstraintEndpoint {
  private final ConstraintQuery constraintQuery;

  public @Nonnull List<@Nonnull ConstraintRequestDTO> findConstraints(@Nonnull List<@Nonnull String> constraintIds) {
    var typedIds = constraintIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findByIds(typedIds);
  }

  public @Nonnull List<@Nonnull EmployeeShiftRequestDTO> findSpecificShiftRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, EmployeeShiftRequestDTO.class);
  }

  public @Nonnull List<@Nonnull ShiftsPerScheduleRequestDTO> findShiftsPerScheduleRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, ShiftsPerScheduleRequestDTO.class);
  }

  public @Nonnull List<@Nonnull ConsecutiveWorkingDaysRequestDTO> findConsecutiveWorkingDaysRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, ConsecutiveWorkingDaysRequestDTO.class);
  }

  public @Nonnull List<@Nonnull EmployeesPerShiftRequestDTO> findEmployeesPerShiftRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, EmployeesPerShiftRequestDTO.class);
  }

  public @Nonnull List<@Nonnull ShiftFollowupRestrictionRequestDTO> findShiftFollowupRestrictionRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, ShiftFollowupRestrictionRequestDTO.class);
  }

  public @Nonnull List<@Nonnull ShiftPatternRequestDTO> findShiftPatternRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, ShiftPatternRequestDTO.class);
  }

  public @Nonnull List<@Nonnull TripleShiftConstraintRequestDTO> findTripleShiftConstraintRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, TripleShiftConstraintRequestDTO.class);
  }

  public @Nonnull List<@Nonnull TeamAssignmentRequestDTO> findTeamAssignmentsConstraintRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, TeamAssignmentRequestDTO.class);
  }

  public @Nonnull List<@Nonnull WeekendRequestDTO> findWeekendConstraintRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, WeekendRequestDTO.class);
  }

  public @Nonnull List<@Nonnull EvenShiftDistributionRequestDTO> findEvenShiftDistributionRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, EvenShiftDistributionRequestDTO.class);
  }
}
