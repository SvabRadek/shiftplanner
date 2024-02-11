package com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request;


import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ConsecutiveWorkingDaysRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsecutiveWorkingDaysRequest extends AbstractMinMaxRequest {
  public static final ConstraintType CONSTRAINT_TYPE = ConstraintType.CONSECUTIVE_WORKING_DAYS;
  private WorkShifts targetShift;
  public ConsecutiveWorkingDaysRequest(
      WorkerId owner,
      WorkShifts targetShift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(CONSTRAINT_TYPE, owner, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
    this.targetShift = targetShift;
  }

  public ConsecutiveWorkingDaysRequest(
      WorkShifts targetShift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax) {
    this(null, targetShift, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
  }
}
