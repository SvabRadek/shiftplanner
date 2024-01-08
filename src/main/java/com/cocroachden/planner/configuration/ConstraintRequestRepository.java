package com.cocroachden.planner.configuration;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ConstraintRequestRepository extends CrudRepository<ConstraintRequestRecord, UUID> {
  List<ConstraintRequestRecord> findByOwnerConfiguration(UUID ownerConfiguration);

  List<ConstraintRequestRecord> findByOwnerConfigurationAndTypeIgnoreCase(UUID ownerConfiguration, String type);
}
