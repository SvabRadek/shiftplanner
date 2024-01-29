package com.cocroachden.planner.constraint.service;

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
  public List<@Nonnull SpecificShiftRequestDTO> getSpecificShiftRequests(List<UUID> uuids) {
    return repository.findByIdIn(uuids).stream()
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

  public List<ShiftsPerScheduleRequest> getShiftsPerScheduleRequests(List<UUID> uuids) {
    return repository.findByIdIn(uuids).stream()
        .map(record -> (ShiftsPerScheduleRequest) record.getRequest())
        .toList();
  }
}
