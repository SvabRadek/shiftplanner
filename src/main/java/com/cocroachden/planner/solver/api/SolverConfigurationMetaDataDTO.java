package com.cocroachden.planner.solver.api;

import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class SolverConfigurationMetaDataDTO {
  public static SolverConfigurationMetaDataDTO from(SolverConfigurationRecord record) {
    return new SolverConfigurationMetaDataDTO(
        record.getName(),
        record.getId(),
        record.getCreatedAt(),
        record.getStartDate(),
        record.getEndDate()
    );
  }
  @Nonnull
  private String name;
  @Nonnull
  private UUID id;
  @Nonnull
  private Instant createdAt;
  @Nonnull
  private LocalDate startDate;
  @Nonnull
  private LocalDate endDate;
}
