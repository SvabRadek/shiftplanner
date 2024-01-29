package com.cocroachden.planner.plannerconfiguration.service;

import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class PlannerConfigurationService {
  private PlannerConfigurationRepository repository;

  public PlannerConfigurationRecord getConfiguration(UUID uuid) {
    return repository.getById(uuid);
  }

  public PlannerConfigurationRecord save(PlannerConfigurationDTO record) {
    return repository.save(PlannerConfigurationRecord.from(record));
  }

  public List<PlannerConfigurationRecord> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .toList();
  }

}
