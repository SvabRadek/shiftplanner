package com.cocroachden.planner.solver.api;

import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SolverSolutionDTO {
  @Nonnull
  private SolutionStatus solutionStatus;
  @Nonnull
  private Double resultScore;
  @Nonnull
  private Integer resultIndex;
  @Nonnull
  private Map<@Nonnull String, @Nonnull Map<@Nonnull LocalDate, @Nonnull WorkShifts>> assignments;
}
