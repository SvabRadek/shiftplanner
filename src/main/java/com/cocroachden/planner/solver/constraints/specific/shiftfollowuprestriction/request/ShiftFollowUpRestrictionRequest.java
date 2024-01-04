package com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request;


import com.cocroachden.planner.lib.ConstraintRequestTypeId;
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
  public static final ConstraintRequestTypeId ID = new ConstraintRequestTypeId("ShiftFollowupRestrictionRequest");
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
}
