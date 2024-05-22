package com.cocroachden.planner.plannerconfiguration.repository;

import com.cocroachden.planner.lib.WorkerId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PlannerConfigurationRepository extends CrudRepository<PlannerConfigurationRecord, UUID> {
  PlannerConfigurationRecord getById(UUID id);
  List<PlannerConfigurationRecord> findByWorkersContaining(WorkerId workerId);
}
