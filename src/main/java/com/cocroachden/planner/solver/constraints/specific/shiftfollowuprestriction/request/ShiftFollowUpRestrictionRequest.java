package com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

@Getter
@JsonTypeName("ShiftFollowUpRestrictionRequest")
public class ShiftFollowUpRestrictionRequest extends AbstractConstraintRequest {
  public static final ConstraintRequestId ID = new ConstraintRequestId("ShiftFollowupRestrictionRequest");
  private final WorkShifts firstShift;
  private final WorkShifts forbiddenFollowup;
  private final Integer penalty;

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
