package com.cocroachden.planner.solver.repository;

import dev.hilla.Nonnull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public interface SolverConfigurationMetadata {
  @Nonnull UUID getId();
  @Nonnull String getName();
  @Nonnull Instant getCreatedAt();
  @Nonnull Instant getLastUpdated();
  @Nonnull LocalDate getStartDate();
  @Nonnull LocalDate getEndDate();
}
