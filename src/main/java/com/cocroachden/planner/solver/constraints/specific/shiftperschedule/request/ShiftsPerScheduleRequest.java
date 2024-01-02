package com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import lombok.Getter;

@Getter
public class ShiftsPerScheduleRequest extends AbstractMinMaxRequest {
  public static final ConstraintRequestId ID = new ConstraintRequestId("ShiftsPerScheduleRequest");
  private final WorkShifts targetShift;
  public ShiftsPerScheduleRequest(
      WorkerId owner,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    this(owner, WorkShifts.WORKING_SHIFTS, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
  }

  public ShiftsPerScheduleRequest(
      WorkerId owner,
      WorkShifts targetShift,
      Integer hardMin,
      Integer softMin,
      Integer minPenalty,
      Integer softMax,
      Integer maxPenalty,
      Integer hardMax
  ) {
    super(ID, owner, hardMin, softMin, minPenalty, softMax, maxPenalty, hardMax);
    this.targetShift = targetShift;
  }
}
