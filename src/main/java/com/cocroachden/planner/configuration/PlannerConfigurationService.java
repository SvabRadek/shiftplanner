package com.cocroachden.planner.configuration;

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

  public @Nonnull PlannerConfigurationRecord getLatestConfiguration() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .max(Comparator.comparing(PlannerConfigurationRecord::getCreatedAt))
        .orElseThrow(() -> new RuntimeException("No planner configuration was found"));
  }

  @Nonnull
  public List<@Nonnull PlannerConfigurationMetaData> getMetaData() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(c -> new PlannerConfigurationMetaData(
            c.getName(),
            c.getId(),
            c.getStartDate(),
            c.getEndDate()
        )).toList();
  }

  public PlannerConfigurationRecord upsert(PlannerConfigurationRecord record) {
    return repository.save(record);
  }

  public PlannerConfigurationRecord saveAsNew(PlannerConfigurationRecord record) {
    record.setId(UUID.randomUUID());
    return repository.save(record);
  }

  public List<PlannerConfigurationRecord> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .toList();
  }


}
