package com.cocroachden.planner.plannerconfiguration.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PlannerConfigurationRepository extends CrudRepository<PlannerConfigurationRecord, UUID> {
  PlannerConfigurationRecord getById(UUID id);
}
