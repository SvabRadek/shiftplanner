package com.cocroachden.planner.constraint.service;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
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

  public void deleteConstraints(List<UUID> uuids) {
    repository.deleteAllById(uuids);
  }
}
