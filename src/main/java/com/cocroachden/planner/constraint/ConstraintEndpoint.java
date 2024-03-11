package com.cocroachden.planner.constraint;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.request.WorkersPerShiftRequest;
import com.cocroachden.planner.solver.constraints.validator.ConstraintValidator;
import com.cocroachden.planner.solver.constraints.validator.ValidatorResult;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class ConstraintEndpoint {
  private final ConstraintRequestRepository constraintRequestRepository;
  private final ConstraintValidator validator;

  public @Nonnull ValidatorResult validate(
      @Nonnull PlannerConfigurationDTO configurationRecord,
      @Nonnull List<@Nonnull SpecificShiftRequestDTO> constraints,
      @Nonnull List<@Nonnull ShiftPatternRequestDTO> constraints1,
      @Nonnull List<@Nonnull EmployeesPerShiftRequestDTO> constraints2,
      @Nonnull List<@Nonnull ConsecutiveWorkingDaysRequestDTO> constraints3,
      @Nonnull List<@Nonnull ShiftFollowupRestrictionRequestDTO> constraints4,
      @Nonnull List<@Nonnull ShiftsPerScheduleRequestDTO> constraints5
  ) {
    var combined = new ArrayList<ConstraintRequestDTO>();
    combined.addAll(constraints);
    combined.addAll(constraints1);
    combined.addAll(constraints2);
    combined.addAll(constraints3);
    combined.addAll(constraints4);
    combined.addAll(constraints5);
    return validator.validate(configurationRecord, combined);
  }

  public @Nonnull List<@Nonnull SpecificShiftRequestDTO> findSpecificShiftRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return this.getRecords(requestIds).stream()
        .map(r -> SpecificShiftRequestDTO.from(r.getId(), (SpecificShiftRequest) r.getRequest()))
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
        .map(r -> EmployeesPerShiftRequestDTO.from(r.getId(), (WorkersPerShiftRequest) r.getRequest()))
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

  public List<ConstraintRequestRecord> getRecords(List<UUID> constraintIds) {
    return StreamSupport.stream(
        constraintRequestRepository.findAllById(constraintIds).spliterator(),
        false
    ).toList();
  }
}
