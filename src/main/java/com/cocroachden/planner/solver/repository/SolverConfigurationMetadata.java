package com.cocroachden.planner.solver.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public interface SolverConfigurationMetadata {
  UUID getId();

  String getName();

  Instant getCreatedAt();

  Instant getLastUpdated();

  LocalDate getStartDate();

  LocalDate getEndDate();
}
