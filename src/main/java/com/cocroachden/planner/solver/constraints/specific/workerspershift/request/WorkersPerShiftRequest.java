package com.cocroachden.planner.solver.constraints.specific.workerspershift.request;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

@Getter
@JsonTypeName("WorkersPerShiftRequest")
public class WorkersPerShiftRequest extends AbstractMinMaxRequest {
  public static final ConstraintRequestId ID = new ConstraintRequestId("WorkersPerShiftRequest");
  private final WorkShifts shift;
  public WorkersPerShiftRequest(
      WorkShifts shift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(ID, null, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
    this.shift = shift;
  }
}
