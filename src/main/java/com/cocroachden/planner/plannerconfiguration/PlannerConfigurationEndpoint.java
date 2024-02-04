package com.cocroachden.planner.plannerconfiguration;

import com.cocroachden.planner.plannerconfiguration.service.PlannerConfigurationService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class PlannerConfigurationEndpoint {
  private final PlannerConfigurationService service;

  @Nonnull
  public List<@Nonnull PlannerConfigurationMetaDataDTO> getMetaData() {
    return service.findAll().stream()
        .map(PlannerConfigurationMetaDataDTO::from)
        .toList();
  }

  @Nonnull
  public List<@Nonnull PlannerConfigurationDTO> findAll() {
    return service.findAll().stream()
        .map(PlannerConfigurationDTO::from)
        .toList();
  }

  @Nonnull
  public PlannerConfigurationDTO getConfiguration(@Nonnull UUID uuid) {
    return PlannerConfigurationDTO.from(
        service.getConfiguration(uuid)
    );
  }

  @Nonnull
  public UUID save(@Nonnull PlannerConfigurationDTO configurationDTO) {
    return service.save(configurationDTO).getId();
  }

  @Nonnull
  public UUID update(@Nonnull PlannerConfigurationDTO configurationDTO) {
    return service.save(configurationDTO).getId();
  }



}
