package com.cocroachden.planner.solver.constraints.specific.shiftpattern.request;


import com.cocroachden.planner.constraint.ShiftPatternRequestDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ShiftPatternConstraintRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShiftPatternConstraintRequest extends AbstractEmployeeSpecificConstraint {
  public static final ConstraintType ID = ConstraintType.SHIFT_PATTERN_CONSTRAINT;

  public static ShiftPatternConstraintRequest from(ShiftPatternRequestDTO dto) {
    return new ShiftPatternConstraintRequest(
        dto.getOwner(),
        dto.getStartDayIndex(),
        dto.getReward(),
        dto.getShiftPattern().toArray(WorkShifts[]::new)
    );
  }

  private Integer startDateIndex;
  private WorkShifts[] shiftPattern;
  private Integer reward;

  public ShiftPatternConstraintRequest(EmployeeId owner, Integer startDateIndex, Integer reward, WorkShifts... shiftPattern) {
    super(ID, owner);
    this.startDateIndex = startDateIndex;
    this.shiftPattern = shiftPattern;
    this.reward = reward;
  }

  public ShiftPatternConstraintRequest(EmployeeId owner, Integer startDateIndex, WorkShifts[] shiftPattern, Integer reward) {
    super(ID, owner);
    this.startDateIndex = startDateIndex;
    this.shiftPattern = shiftPattern;
    this.reward = reward;
  }

  public ShiftPatternConstraintRequest(Integer reward, Integer startDateIndex, WorkShifts... shiftPattern) {
    this(null, startDateIndex, reward, shiftPattern);
  }


  @Override
  public String toString() {
    return "{ " +
        "type: " + this.getType() +
        ", startDateIndex: " + startDateIndex +
        ", shiftPattern: HIDDEN" +
        ", reward: " + reward +
        " }";
  }
}
