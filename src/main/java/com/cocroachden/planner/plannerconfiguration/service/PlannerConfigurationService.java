package com.cocroachden.planner.plannerconfiguration.service;

import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRepository;
import jakarta.transaction.Transactional;
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

  public PlannerConfigurationRecord save(PlannerConfigurationDTO dto) {
    return repository.save(PlannerConfigurationRecord.from(UUID.randomUUID(), dto));
  }

  public PlannerConfigurationRecord update(PlannerConfigurationDTO dto) {
    return repository.save(PlannerConfigurationRecord.from(dto.getId(), dto));
  }

  public List<PlannerConfigurationRecord> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .toList();
  }

}
