package com.cocroachden.planner.constraint;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.service.ConstraintRequestService;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.request.WorkersPerShiftRequest;
import com.cocroachden.planner.solver.constraints.validator.ConstraintValidator;
import com.cocroachden.planner.solver.constraints.validator.ValidatorResult;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class ConstraintEndpoint {
  private final ConstraintRequestService service;
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
    return service.getRecords(requestIds).stream()
        .map(r -> SpecificShiftRequestDTO.from(r.getId(), (SpecificShiftRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull UUID> saveAllSpecificShiftRequests(
      @Nonnull List<@Nonnull SpecificShiftRequestDTO> requestDTOs
  ) {
    return requestDTOs.stream()
        .map(requestDTO ->
            new ConstraintRequestRecord(
                new SpecificShiftRequest(
                    new WorkerId(requestDTO.getOwner()),
                    requestDTO.getDate().toDate(),
                    requestDTO.getRequestedShift()
                )
            )
        ).map(record -> service.saveAsNew(record).getId())
        .toList();
  }

  public @Nonnull List<@Nonnull ShiftsPerScheduleRequestDTO> findShiftsPerScheduleRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return service.getRecords(requestIds).stream()
        .map(r -> ShiftsPerScheduleRequestDTO.from(r.getId(), (ShiftsPerScheduleRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull UUID> saveAllShiftsPerScheduleRequests(
      @Nonnull List<@Nonnull ShiftsPerScheduleRequestDTO> requestDTOs
  ) {
    return requestDTOs.stream().map(requestDTO ->
            new ConstraintRequestRecord(
                new ShiftsPerScheduleRequest(
                    requestDTO.getOwner(),
                    requestDTO.getTargetShift(),
                    requestDTO.getHardMin(),
                    requestDTO.getSoftMin(),
                    requestDTO.getMinPenalty(),
                    requestDTO.getSoftMax(),
                    requestDTO.getMaxPenalty(),
                    requestDTO.getHardMax()
                )
            )
        ).map(request -> service.saveAsNew(request).getId())
        .toList();
  }

  public @Nonnull List<@Nonnull ConsecutiveWorkingDaysRequestDTO> findConsecutiveWorkingDaysRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return service.getRecords(requestIds).stream()
        .map(r -> ConsecutiveWorkingDaysRequestDTO.from(r.getId(), (ConsecutiveWorkingDaysRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull UUID> saveAllConsecutiveWorkingDaysRequests(
      @Nonnull List<@Nonnull ConsecutiveWorkingDaysRequestDTO> requestDTOs
  ) {
    return requestDTOs.stream().map(requestDTO ->
            new ConstraintRequestRecord(
                new ConsecutiveWorkingDaysRequest(
                    requestDTO.getTargetShift(),
                    requestDTO.getHardMin(),
                    requestDTO.getSoftMin(),
                    requestDTO.getMinPenalty(),
                    requestDTO.getSoftMax(),
                    requestDTO.getMaxPenalty(),
                    requestDTO.getHardMax()
                )
            )
        ).map(request -> service.saveAsNew(request).getId())
        .toList();
  }

  public @Nonnull List<@Nonnull EmployeesPerShiftRequestDTO> findEmployeesPerShiftRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return service.getRecords(requestIds).stream()
        .map(r -> EmployeesPerShiftRequestDTO.from(r.getId(), (WorkersPerShiftRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull UUID> saveAllEmployeesPerShiftRequests(
      @Nonnull List<@Nonnull EmployeesPerShiftRequestDTO> requestDTOs
  ) {
    return requestDTOs.stream().map(requestDTO ->
            new ConstraintRequestRecord(
                new WorkersPerShiftRequest(
                    requestDTO.getTargetShift(),
                    requestDTO.getHardMin(),
                    requestDTO.getSoftMin(),
                    requestDTO.getMinPenalty(),
                    requestDTO.getSoftMax(),
                    requestDTO.getMaxPenalty(),
                    requestDTO.getHardMax()
                )
            )
        ).map(request -> service.saveAsNew(request).getId())
        .toList();
  }

  public @Nonnull List<@Nonnull ShiftFollowupRestrictionRequestDTO> findShiftFollowupRestrictionRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return service.getRecords(requestIds).stream()
        .map(r -> ShiftFollowupRestrictionRequestDTO.from(r.getId(), (ShiftFollowUpRestrictionRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull UUID> saveShiftFollowupRestrictionRequests(
      @Nonnull List<@Nonnull ShiftFollowupRestrictionRequestDTO> requestDTOs
  ) {
    return requestDTOs.stream().map(requestDTO -> {
          var record = new ConstraintRequestRecord();
          record.setRequest(
              new ShiftFollowUpRestrictionRequest(
                  requestDTO.getFirstShift(),
                  requestDTO.getForbiddenFollowup(),
                  requestDTO.getPenalty()
              )
          );
          return record;
        }).map(request -> service.saveAsNew(request).getId())
        .toList();
  }

  public @Nonnull List<@Nonnull ShiftPatternRequestDTO> findShiftPatternRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return service.getRecords(requestIds).stream()
        .map(r -> ShiftPatternRequestDTO.from(r.getId(), (ShiftPatternConstraintRequest) r.getRequest()))
        .toList();
  }

  public @Nonnull List<@Nonnull UUID> saveShiftPatternRequests(
      @Nonnull List<@Nonnull ShiftPatternRequestDTO> requestDTOs
  ) {
    return requestDTOs.stream().map(requestDTO ->
            new ConstraintRequestRecord(
                new ShiftPatternConstraintRequest(
                    requestDTO.getWorkerId(),
                    requestDTO.getStartDayIndex(),
                    requestDTO.getReward(),
                    requestDTO.getShiftPattern().toArray(WorkShifts[]::new)
                )
            )
        ).map(request -> service.saveAsNew(request).getId())
        .toList();
  }

}
