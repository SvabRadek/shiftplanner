package com.cocroachden.planner.plannerconfiguration.service;

import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationMetaDataDTO;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class PlannerConfigurationService {
  private PlannerConfigurationRepository repository;

  public @Nonnull PlannerConfigurationDTO getLatestConfiguration() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .max(Comparator.comparing(PlannerConfigurationRecord::getCreatedAt))
        .map(PlannerConfigurationDTO::from)
        .orElseThrow(() -> new RuntimeException("No planner configuration was found"));
  }

  public @Nonnull PlannerConfigurationDTO getConfiguration(UUID uuid) {
    return PlannerConfigurationDTO.from(repository.getById(uuid));
  }

  @Nonnull
  public List<@Nonnull PlannerConfigurationMetaDataDTO> getMetaData() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(c -> new PlannerConfigurationMetaDataDTO(
            c.getName(),
            c.getId(),
            c.getStartDate(),
            c.getEndDate()
        )).toList();
  }

  public PlannerConfigurationDTO upsert(PlannerConfigurationDTO record) {
    return PlannerConfigurationDTO.from(repository.save(PlannerConfigurationRecord.from(record)));
  }

  public PlannerConfigurationDTO saveAsNew(PlannerConfigurationDTO record) {
    return PlannerConfigurationDTO.from(repository.save(PlannerConfigurationRecord.from(record)));
  }

  public List<PlannerConfigurationDTO> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(PlannerConfigurationDTO::from)
        .toList();
  }

}
