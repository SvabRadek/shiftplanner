package com.cocroachden.planner.solver.constraints.specific;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.lib.WorkerId;
import lombok.Getter;

@Getter
public class AbstractMinMaxRequest extends AbstractConstraintRequest {
  private final Integer softMin;
  private final Integer minPenalty;
  private final Integer hardMin;
  private final Integer softMax;
  private final Integer maxPenalty;
  private final Integer hardMax;
  public AbstractMinMaxRequest(
      ConstraintRequestId constraintRequestId,
      WorkerId owner,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(constraintRequestId, owner);
    this.softMin = softMin;
    this.minPenalty = minPenalty;
    this.hardMin = hardMin;
    this.softMax = softMax;
    this.maxPenalty = maxPenalty;
    this.hardMax = hardMax;
  }
}
