package com.cocroachden.planner.constraint.service;

import com.cocroachden.planner.constraint.ShiftsPerScheduleRequestDTO;
import com.cocroachden.planner.constraint.SpecificShiftRequestDTO;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.lib.StupidDate;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class ConstraintRequestService {
  private ConstraintRequestRepository repository;

  @Nonnull
  public List<@Nonnull SpecificShiftRequestDTO> getSpecificShiftRequests(UUID ownerConfiguration) {
    return repository.findByOwnerConfigurationAndTypeIgnoreCase(
            ownerConfiguration,
            SpecificShiftRequest.ID.getId()
        ).stream()
        .map(record -> {
          var request = (SpecificShiftRequest) record.getRequest();
          return new SpecificShiftRequestDTO(
              record.getType(),
              request.getOwner().orElseThrow().getWorkerId(),
              StupidDate.fromDate(request.getDate()),
              request.getRequestedShift()
          );
        }).toList();
  }

  public ConstraintRequestRecord upsert(ConstraintRequestRecord record) {
    return repository.save(record);
  }

  public ConstraintRequestRecord saveAsNew(ConstraintRequestRecord record) {
    record.setId(UUID.randomUUID());
    return repository.save(record);
  }

  public List<ShiftsPerScheduleRequestDTO> getShiftsPerScheduleRequests(UUID plannerConfigId) {
    return repository.findByOwnerConfigurationAndTypeIgnoreCase(
            plannerConfigId,
            ShiftsPerScheduleRequest.ID.getId()
        ).stream()
        .map(record -> {
          var constraint = (ShiftsPerScheduleRequest) record.getRequest();
          return new ShiftsPerScheduleRequestDTO(
              record.getId(),
              constraint.getOwner().orElseThrow(),
              constraint.getTargetShift(),
              constraint.getHardMin(),
              constraint.getSoftMin(),
              constraint.getMinPenalty(),
              constraint.getSoftMax(),
              constraint.getMaxPenalty(),
              constraint.getHardMax()
          );
        }).toList();
  }
}
