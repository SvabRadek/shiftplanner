package com.cocroachden.planner.solver.constraints.specific.shiftperday.request;


import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("OneShiftPerDayRequest")
public class OneShiftPerDayRequest implements ConstraintRequest {
  @Override
  public ConstraintType getType() {
    return ConstraintType.ONE_SHIFT_PER_DAY;
  }

  @Override
  public String toString() {
    return "{ type: " + this.getType() + " }";
  }
}
