package com.cocroachden.planner.plannerconfiguration;

import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlannerConfigurationMetaDataDTO {
  @Nonnull
  private String name;
  @Nonnull
  private UUID id;
  @Nonnull
  private LocalDate startDate;
  @Nonnull
  private LocalDate endDate;
}
