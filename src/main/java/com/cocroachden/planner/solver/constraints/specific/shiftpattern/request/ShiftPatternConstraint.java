package com.cocroachden.planner.solver.constraints.specific.shiftpattern.request;


import com.cocroachden.planner.constraint.ShiftPatternConstraintDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ShiftPatternConstraint")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShiftPatternConstraint implements EmployeeConstraint {
  public static ShiftPatternConstraint from(ShiftPatternConstraintDTO dto) {
    return new ShiftPatternConstraint(
            EmployeeId.from(dto.getOwner()),
            dto.getStartDayIndex(),
            dto.getReward(),
            dto.getShiftPattern().toArray(WorkShifts[]::new)
    );
  }

  private final ConstraintType type = ConstraintType.SHIFT_PATTERN_CONSTRAINT;
  private EmployeeId owner;
  private Integer startDateIndex;
  private WorkShifts[] shiftPattern;
  private Integer reward;

  public ShiftPatternConstraint(EmployeeId owner, Integer startDateIndex, Integer reward, WorkShifts... shiftPattern) {
    this.owner = owner;
    this.startDateIndex = startDateIndex;
    this.shiftPattern = shiftPattern;
    this.reward = reward;
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
