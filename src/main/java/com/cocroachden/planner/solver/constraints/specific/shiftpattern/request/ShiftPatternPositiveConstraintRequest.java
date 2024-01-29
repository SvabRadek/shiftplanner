package com.cocroachden.planner.solver.constraints.specific.shiftpattern.request;


import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ShiftPatternPositiveConstraintRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShiftPatternPositiveConstraintRequest extends AbstractConstraintRequest {
  public static final ConstraintType ID = ConstraintType.SHIFT_PATTERN_POSITIVE_CONSTRAINT;
  private WorkShifts[] shiftPattern;
  private Integer reward;

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
