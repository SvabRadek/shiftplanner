package com.cocroachden.planner.solver.constraints.specific;


import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("AbstractMinMaxConstraint")
public abstract class AbstractMinMaxConstraint implements SolverConstraint {
  private Integer softMin;
  private Integer minPenalty;
  private Integer hardMin;
  private Integer softMax;
  private Integer maxPenalty;
  private Integer hardMax;
  public AbstractMinMaxConstraint(
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    this.softMin = softMin;
    this.minPenalty = minPenalty;
    this.hardMin = hardMin;
    this.softMax = softMax;
    this.maxPenalty = maxPenalty;
    this.hardMax = hardMax;
  }
}
