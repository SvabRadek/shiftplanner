package com.cocroachden.planner.constraint.repository;

import com.cocroachden.planner.constraint.ConstraintRequestRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ConstraintRequestRepository extends CrudRepository<ConstraintRequestRecord, UUID> {
  List<ConstraintRequestRecord> findByOwnerConfiguration(UUID ownerConfiguration);

  List<ConstraintRequestRecord> findByOwnerConfigurationAndTypeIgnoreCase(UUID ownerConfiguration, String type);
}
