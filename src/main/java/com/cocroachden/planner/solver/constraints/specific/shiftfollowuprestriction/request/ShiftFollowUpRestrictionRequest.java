package com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request;


import com.cocroachden.planner.constraint.ShiftFollowupRestrictionRequestDTO;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ShiftFollowUpRestrictionRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShiftFollowUpRestrictionRequest implements EmployeeConstraint {
  public static ShiftFollowUpRestrictionRequest from(ShiftFollowupRestrictionRequestDTO dto) {
    return new ShiftFollowUpRestrictionRequest(
            EmployeeId.from(dto.getOwner()),
            dto.getFirstShift(),
            dto.getForbiddenFollowup(),
            dto.getPenalty()
    );

  }

  private final ConstraintType type = ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION;
  private EmployeeId owner;
  private WorkShifts firstShift;
  private WorkShifts forbiddenFollowup;
  private Integer penalty;

  @Override
  public String toString() {
    return "{ " +
        "type: " + this.getType() +
        ", firstShift: " + firstShift +
        ", forbiddenFollowup: " + forbiddenFollowup +
        ", penalty: " + penalty +
        " }";
  }

}
