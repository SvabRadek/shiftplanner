package com.cocroachden.planner.solver;

import com.cocroachden.planner.lib.StupidDate;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScheduleResultDTO {
  @Nonnull
  private SolutionStatus solutionStatus;
  @Nonnull
  private Double resultScore;
  @Nonnull
  private Integer resultIndex;
  @Nonnull
  private Map<@Nonnull Long, @Nonnull Map<@Nonnull StupidDate,@Nonnull WorkShifts>> assignments;
}
