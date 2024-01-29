package com.cocroachden.planner.plannerconfiguration;

import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlannerConfigurationMetaDataDTO {
  public static PlannerConfigurationMetaDataDTO from(PlannerConfigurationRecord record) {
    return new PlannerConfigurationMetaDataDTO(
        record.getName(),
        record.getId(),
        record.getStartDate(),
        record.getEndDate()
    );
  }
  @Nonnull
  private String name;
  @Nonnull
  private UUID id;
  @Nonnull
  private LocalDate startDate;
  @Nonnull
  private LocalDate endDate;
}
