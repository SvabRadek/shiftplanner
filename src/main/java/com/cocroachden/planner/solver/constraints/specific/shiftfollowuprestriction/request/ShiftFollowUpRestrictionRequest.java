package com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request;


import com.cocroachden.planner.constraint.api.ShiftFollowupRestrictionRequestDTO;
import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonTypeName("ShiftFollowUpRestrictionRequest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShiftFollowUpRestrictionRequest extends AbstractConstraintRequest {
  public static final ConstraintType ID = ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION;

  public static ShiftFollowUpRestrictionRequest from(ShiftFollowupRestrictionRequestDTO dto) {
    return new ShiftFollowUpRestrictionRequest(
        dto.getFirstShift(),
        dto.getForbiddenFollowup(),
        dto.getPenalty()
    );

  }

  private WorkShifts firstShift;
  private WorkShifts forbiddenFollowup;
  private Integer penalty;

  public ShiftFollowUpRestrictionRequest(WorkShifts firstShift, WorkShifts forbiddenFollowup) {
    this(null, firstShift, forbiddenFollowup, 0);
  }

  public ShiftFollowUpRestrictionRequest(WorkShifts firstShift, WorkShifts forbiddenFollowup, Integer penalty) {
    this(null, firstShift, forbiddenFollowup, penalty);
  }

  public ShiftFollowUpRestrictionRequest(WorkerId owner, WorkShifts firstShift, WorkShifts forbiddenFollowup, Integer penalty) {
    super(ID, owner);
    this.firstShift = firstShift;
    this.forbiddenFollowup = forbiddenFollowup;
    this.penalty = penalty;
  }


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
