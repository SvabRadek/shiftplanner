package com.cocroachden.planner.solver.constraints.specific.shiftperday.request;


import com.cocroachden.planner.lib.ConstraintRequestTypeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("OneShiftPerDayRequest")
public class OneShiftPerDayRequest extends AbstractConstraintRequest {
  public static final ConstraintRequestTypeId ID = new ConstraintRequestTypeId("OneShiftPerDayRequest");
  public OneShiftPerDayRequest() {
    super(ID, null);
  }
}
