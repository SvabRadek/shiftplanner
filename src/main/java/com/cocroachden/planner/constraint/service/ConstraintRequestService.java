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
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class ConstraintRequestService {
  private ConstraintRequestRepository repository;

  public List<ConstraintRequestRecord> getRecords(List<UUID> constraintIds) {
    return StreamSupport.stream(
            repository.findAllById(constraintIds).spliterator(),
            false
        ).toList();
  }

  public List<SpecificShiftRequestDTO> getSpecificShiftRequests(List<UUID> uuids) {
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
