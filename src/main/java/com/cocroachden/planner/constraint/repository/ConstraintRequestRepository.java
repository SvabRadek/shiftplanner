package com.cocroachden.planner.constraint.repository;

import com.cocroachden.planner.constraint.ConstraintId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ConstraintRequestRepository extends CrudRepository<ConstraintRequestRecord, ConstraintId>, JpaSpecificationExecutor<ConstraintRequestRecord> {
  List<ConstraintRequestRecord> findByIdIn(Collection<ConstraintId> ids);
}
