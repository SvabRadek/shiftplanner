package com.cocroachden.planner.configuration;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PlannerConfigurationRepository extends CrudRepository<PlannerConfigurationRecord, UUID> {
}
