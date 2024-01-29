package com.cocroachden.planner.constraint.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConstraintRequestRepository extends CrudRepository<ConstraintRequestRecord, UUID> {

  List<ConstraintRequestRecord> findByIdIn(Collection<UUID> ids);
}
