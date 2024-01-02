package com.cocroachden.planner.solver.constraints.specific.shiftpattern.request;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import lombok.Getter;

@Getter
public class ShiftPatternPositiveConstraintRequest extends AbstractConstraintRequest {
  public static final ConstraintRequestId ID = new ConstraintRequestId("ShiftPatternPositiveConstraintRequest");
  private final WorkShifts[] shiftPattern;
  private final Integer reward;

  public ShiftPatternPositiveConstraintRequest(WorkerId owner, Integer reward, WorkShifts... shiftPattern) {
    super(ID, owner);
    this.shiftPattern = shiftPattern;
    this.reward = reward;
  }

  public ShiftPatternPositiveConstraintRequest(WorkerId owner, WorkShifts[] shiftPattern, Integer reward) {
    super(ID, owner);
    this.shiftPattern = shiftPattern;
    this.reward = reward;
  }

  public ShiftPatternPositiveConstraintRequest(Integer reward, WorkShifts... shiftPattern) {
    this(null, reward, shiftPattern);
  }
}
