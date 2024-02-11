package com.cocroachden.planner.constraint.service;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternPositiveConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.request.WorkersPerShiftRequest;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
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

  public List<SpecificShiftRequest> getSpecificShiftRequests(List<UUID> uuids) {
    return this.getRecords(uuids).stream()
        .map(record -> (SpecificShiftRequest) record.getRequest())
        .toList();
  }

  public List<ShiftsPerScheduleRequest> getShiftsPerScheduleRequests(List<UUID> uuids) {
    return this.getRecords(uuids).stream()
        .map(record -> (ShiftsPerScheduleRequest) record.getRequest())
        .toList();
  }

  public List<ConsecutiveWorkingDaysRequest> getConsecutiveDayRequests(List<UUID> uuids) {
    return this.getRecords(uuids).stream()
        .map(record -> (ConsecutiveWorkingDaysRequest) record.getRequest())
        .toList();
  }

  public List<WorkersPerShiftRequest> getEmployeesPerShiftRequests(List<UUID> uuids) {
    return this.getRecords(uuids).stream()
        .map(record -> (WorkersPerShiftRequest) record.getRequest())
        .toList();
  }

  public List<ShiftFollowUpRestrictionRequest> getShiftFollowupRestrictionRequests(List<UUID> uuids) {
    return this.getRecords(uuids).stream()
        .map(record -> (ShiftFollowUpRestrictionRequest) record.getRequest())
        .toList();
  }

  public List<ShiftPatternPositiveConstraintRequest> getShiftPatternRequests(List<UUID> uuids) {
    return this.getRecords(uuids).stream()
        .map(record -> (ShiftPatternPositiveConstraintRequest) record.getRequest())
        .toList();
  }

  public ConstraintRequestRecord upsert(ConstraintRequestRecord record) {
    return repository.findById(record.getId())
        .map(present -> {
          present.setType(record.getType());
          present.setRequest(record.getRequest());
          return repository.save(present);
        }).orElseThrow(
            () -> new NoSuchElementException(
                "ConstraintRequestRecord with id %s was not found.".formatted(record.getId())
            )
        );
  }

  public ConstraintRequestRecord saveAsNew(ConstraintRequestRecord record) {
    record.setId(UUID.randomUUID());
    return repository.save(record);
  }
}
