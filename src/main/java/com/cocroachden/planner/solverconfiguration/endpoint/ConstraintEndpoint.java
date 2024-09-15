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

  public @Nonnull List<@Nonnull ConstraintDTO> findConstraints(@Nonnull List<@Nonnull String> constraintIds) {
    var typedIds = constraintIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findByIds(typedIds);
  }

  public @Nonnull List<@Nonnull EmployeeShiftConstraintDTO> findSpecificShiftRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, EmployeeShiftConstraintDTO.class);
  }

  public @Nonnull List<@Nonnull ShiftsPerScheduleConstraintDTO> findShiftsPerScheduleRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, ShiftsPerScheduleConstraintDTO.class);
  }

  public @Nonnull List<@Nonnull ConsecutiveWorkingDaysConstraintDTO> findConsecutiveWorkingDaysRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, ConsecutiveWorkingDaysConstraintDTO.class);
  }

  public @Nonnull List<@Nonnull EmployeesPerShiftConstraintDTO> findEmployeesPerShiftRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, EmployeesPerShiftConstraintDTO.class);
  }

  public @Nonnull List<@Nonnull ShiftFollowupRestrictionConstraintDTO> findShiftFollowupRestrictionRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, ShiftFollowupRestrictionConstraintDTO.class);
  }

  public @Nonnull List<@Nonnull ShiftPatternConstraintDTO> findShiftPatternRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, ShiftPatternConstraintDTO.class);
  }

  public @Nonnull List<@Nonnull TripleShiftConstraintDTO> findTripleShiftConstraintRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, TripleShiftConstraintDTO.class);
  }

  public @Nonnull List<@Nonnull TeamAssignmentConstraintDTO> findTeamAssignmentsConstraintRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, TeamAssignmentConstraintDTO.class);
  }

  public @Nonnull List<@Nonnull WeekendConstraintDTO> findWeekendConstraintRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, WeekendConstraintDTO.class);
  }

  public @Nonnull List<@Nonnull EvenShiftDistributionConstraintDTO> findEvenShiftDistributionRequests(
      @Nonnull List<@Nonnull String> requestIds
  ) {
    var typedIds = requestIds.stream().map(ConstraintId::new).toList();
    return constraintQuery.findSpecificById(typedIds, EvenShiftDistributionConstraintDTO.class);
  }
}
