package com.cocroachden.planner.constraint;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.service.ConstraintRequestService;
import com.cocroachden.planner.lib.WorkerId;
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
      @Nonnull UUID plannerConfigId
  ) {
    return service.getSpecificShiftRequests(plannerConfigId);
  }

  public void saveSpecificShiftRequest(
      @Nonnull UUID plannerConfigId,
      @Nonnull SpecificShiftRequestDTO requestDTO
  ) {
    var request = new ConstraintRequestRecord(
        plannerConfigId,
        new SpecificShiftRequest(
            new WorkerId(requestDTO.getOwner()),
            requestDTO.getDate().toDate(),
            requestDTO.getRequestedShift()
        )
    );
    service.saveAsNew(request);
  }

  public @Nonnull List<@Nonnull ShiftsPerScheduleRequestDTO> findShiftsPerScheduleRequests(
      @Nonnull UUID plannerConfigId
  ) {
    return service.getShiftsPerScheduleRequests(plannerConfigId);
  }

  public void saveShiftsPerScheduleRequests(
      @Nonnull UUID plannerConfigId,
      @Nonnull ShiftsPerScheduleRequestDTO requestDTO
  ) {
    var request = new ConstraintRequestRecord(
        plannerConfigId,
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
    );
    service.saveAsNew(request);
  }
}
