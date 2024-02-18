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
@JsonTypeName("ShiftPatternConstraintRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShiftPatternConstraintRequest extends AbstractConstraintRequest {
  public static final ConstraintType ID = ConstraintType.SHIFT_PATTERN_CONSTRAINT;
  private Integer startDateIndex;
  private WorkShifts[] shiftPattern;
  private Integer reward;

  public ShiftPatternConstraintRequest(WorkerId owner, Integer startDateIndex, Integer reward, WorkShifts... shiftPattern) {
    super(ID, owner);
    this.startDateIndex = startDateIndex;
    this.shiftPattern = shiftPattern;
    this.reward = reward;
  }

  public ShiftPatternConstraintRequest(WorkerId owner, Integer startDateIndex, WorkShifts[] shiftPattern, Integer reward) {
    super(ID, owner);
    this.startDateIndex = startDateIndex;
    this.shiftPattern = shiftPattern;
    this.reward = reward;
  }

  public ShiftPatternConstraintRequest(Integer reward, Integer startDateIndex, WorkShifts... shiftPattern) {
    this(null, startDateIndex, reward, shiftPattern);
  }
}
