package com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

@Getter
@JsonTypeName("ConsecutiveWorkingDaysRequest")
public class ConsecutiveWorkingDaysRequest extends AbstractMinMaxRequest {
  public static final ConstraintRequestId ID = new ConstraintRequestId("ConsecutiveWorkingDaysRequest");

  public ConsecutiveWorkingDaysRequest(
      WorkerId owner,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(ID, owner, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
  }

  public ConsecutiveWorkingDaysRequest(
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    this(null, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
  }
}
