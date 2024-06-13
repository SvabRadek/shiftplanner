package com.cocroachden.planner.solver.api;

import com.cocroachden.planner.core.StupidDate;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  private Map<@Nonnull Long, @Nonnull Map<@Nonnull StupidDate,@Nonnull WorkShifts>> assignments;
}
