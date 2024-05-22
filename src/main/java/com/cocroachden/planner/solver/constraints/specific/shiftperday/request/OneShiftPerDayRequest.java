package com.cocroachden.planner.solver.constraints.specific.shiftperday.request;


import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("OneShiftPerDayRequest")
public class OneShiftPerDayRequest extends AbstractConstraintRequest {
  public static final ConstraintType ID = ConstraintType.ONE_SHIFT_PER_DAY;

  public OneShiftPerDayRequest() {
    super(ID, null);
  }


  @Override
  public String toString() {
    return "{ type: " + this.getType() + ", owner: " + (this.getOwner().isPresent() ? this.getOwner().toString() : "") + " }";
  }
}
