package com.cocroachden.planner.solver.api;

import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.core.StupidDate;
import com.cocroachden.planner.core.identity.WorkerId;
import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SolverConfigurationDTO {
  public static SolverConfigurationDTO from(SolverConfigurationRecord record) {
    return new SolverConfigurationDTO(
        record.getId(),
        record.getName(),
        record.getCreatedAt(),
        record.getLastUpdated(),
        StupidDate.fromDate(record.getStartDate()),
        StupidDate.fromDate(record.getEndDate()),
        record.getWorkers(),
        record.getConstraintRequestRecords().stream()
            .map(ConstraintMapper::fromRecord)
            .toList()
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
  private StupidDate startDate;
  @Nonnull
  private StupidDate endDate;
  @Nonnull
  private List<@Nonnull WorkerId> workers;
  @Nonnull
  private List<@Nonnull ConstraintRequestDTO> constraints;
}
