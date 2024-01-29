package com.cocroachden.planner.plannerconfiguration;

import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlannerConfigurationDTO {
  public static PlannerConfigurationDTO from(PlannerConfigurationRecord record) {
    return new PlannerConfigurationDTO(
        record.getId(),
        record.getName(),
        record.getCreatedAt(),
        record.getLastUpdated(),
        record.getStartDate(),
        record.getEndDate(),
        record.getWorkers(),
        record.getConstraintRequestInstances().stream()
            .map(link -> new ConfigurationRequestLinkDTO(
                link.getRequestType(),
                link.getRequestId()
            )).toList()
    );
  }

  @Nonnull
  private UUID id;
  @Nonnull
  private String name;
  @Nonnull
  private Instant createdAt;
  @Nonnull
  private Instant lastUpdated;
  @Nonnull
  private LocalDate startDate;
  @Nonnull
  private LocalDate endDate;
  @Nonnull
  private List<@Nonnull WorkerId> workers;
  @Nonnull
  private List<@Nonnull ConfigurationRequestLinkDTO> constraintRequestInstances;
}
