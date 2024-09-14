package com.cocroachden.planner.solver.constraints.specific.shiftpattern.request;


import com.cocroachden.planner.constraint.ShiftPatternRequestDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ShiftPatternConstraintRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShiftPatternConstraintRequest implements EmployeeConstraint {
  public static ShiftPatternConstraintRequest from(ShiftPatternRequestDTO dto) {
    return new ShiftPatternConstraintRequest(
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

  public ShiftPatternConstraintRequest(EmployeeId owner, Integer startDateIndex, Integer reward, WorkShifts... shiftPattern) {
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
