package com.cocroachden.planner.constraint;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.service.ConstraintRequestService;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class ConstraintEndpoint {
  private final ConstraintRequestService service;

  public @Nonnull List<@Nonnull SpecificShiftRequestDTO> findSpecificShiftRequests(
      @Nonnull List<@Nonnull UUID> requestIds
  ) {
    return service.getSpecificShiftRequests(requestIds).stream()
        .map(SpecificShiftRequestDTO::from)
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
    return service.getShiftsPerScheduleRequests(requestIds).stream()
        .map(ShiftsPerScheduleRequestDTO::from)
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
    return service.getConsecutiveDayRequests(requestIds).stream()
        .map(ConsecutiveWorkingDaysRequestDTO::from)
        .toList();
  }

  public @Nonnull List<@Nonnull UUID> saveAllConsecutiveWorkingDaysRequests(
      @Nonnull List<@Nonnull ConsecutiveWorkingDaysRequestDTO> requestDTOs
  ) {
    return requestDTOs.stream().map(requestDTO ->
            new ConstraintRequestRecord(
                new ConsecutiveWorkingDaysRequest(
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

}
