package com.cocroachden.planner.solver.constraints.specific.shiftperday.request;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;

public class OneShiftPerDayRequest extends AbstractConstraintRequest {
  public static final ConstraintRequestId ID = new ConstraintRequestId("OneShiftPerDayRequest");
  public OneShiftPerDayRequest() {
    super(ID, null);
  }
}
