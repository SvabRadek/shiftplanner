package com.cocroachden.planner.plannerconfiguration.service;

import com.cocroachden.planner.constraint.service.ConstraintRequestService;
import com.cocroachden.planner.plannerconfiguration.ConfigurationRequestLinkDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.plannerconfiguration.repository.ConfigurationRequestLink;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.time.Instant;
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

  public PlannerConfigurationRecord save(PlannerConfigurationDTO dto) throws IllegalArgumentException {
    this.ensureAllConstraintsAreInDatabase(dto);
    return repository.save(PlannerConfigurationRecord.from(UUID.randomUUID(), dto));
  }

  @Transactional
  public PlannerConfigurationRecord update(PlannerConfigurationDTO dto) throws IllegalArgumentException {
    this.ensureAllConstraintsAreInDatabase(dto);
    var record = repository.getById(dto.getId());
    record.setName(dto.getName());
    record.setWorkers(dto.getWorkers());
    record.setStartDate(dto.getStartDate().toDate());
    record.setEndDate(dto.getEndDate().toDate());
    record.setConstraintRequestInstances(
        dto.getConstraintRequestInstances().stream()
            .map(ConfigurationRequestLink::from)
            .toList()
    );
    record.setLastUpdated(Instant.now());
    return repository.save(record);
  }

  private void ensureAllConstraintsAreInDatabase(PlannerConfigurationDTO dto) throws IllegalArgumentException {
    var constraints = constraintRequestService.getRecords(
        dto.getConstraintRequestInstances().stream()
            .map(ConfigurationRequestLinkDTO::getRequestId)
            .toList()
    );
    if (constraints.size() != dto.getConstraintRequestInstances().size()) {
      throw new IllegalArgumentException("Not all constraints linked to this configuration exist in database.");
    }
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
