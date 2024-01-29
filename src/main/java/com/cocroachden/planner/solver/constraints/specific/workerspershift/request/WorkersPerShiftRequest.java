package com.cocroachden.planner.solver.constraints.specific.workerspershift.request;


import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("WorkersPerShiftRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkersPerShiftRequest extends AbstractMinMaxRequest {
  public static final ConstraintType ID = ConstraintType.WORKERS_PER_SHIFT;
  private WorkShifts shift;
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
