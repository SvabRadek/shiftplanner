package com.cocroachden.planner.plannerconfiguration.service;

import com.cocroachden.planner.constraint.service.ConstraintRequestService;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.plannerconfiguration.repository.ConfigurationRequestLink;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@AllArgsConstructor
public class PlannerConfigurationService {
  private PlannerConfigurationRepository repository;
  private ConstraintRequestService constraintRequestService;

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

  public void delete(UUID uuid) {
    var config = getConfiguration(uuid);
    var constraintIds = config.getConstraintRequestInstances().stream()
        .map(ConfigurationRequestLink::getRequestId)
        .toList();
    constraintRequestService.deleteConstraints(constraintIds);
    repository.deleteById(uuid);
  }

}
