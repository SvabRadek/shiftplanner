package com.cocroachden.planner.solver.constraints;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternPositiveConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificNegativeShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.request.WorkersPerShiftRequest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "constraint_type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SpecificShiftRequest.class, name = "SpecificShiftRequest"),
    @JsonSubTypes.Type(value = OneShiftPerDayRequest.class, name = "OneShiftPerDayRequest"),
    @JsonSubTypes.Type(value = ShiftFollowUpRestrictionRequest.class, name = "ShiftFollowUpRestrictionRequest"),
    @JsonSubTypes.Type(value = ShiftPatternPositiveConstraintRequest.class, name = "ShiftPatternPositiveConstraintRequest"),
    @JsonSubTypes.Type(value = ShiftsPerScheduleRequest.class, name = "ShiftsPerScheduleRequest"),
    @JsonSubTypes.Type(value = SpecificNegativeShiftRequest.class, name = "SpecificNegativeShiftRequest"),
    @JsonSubTypes.Type(value = WorkersPerShiftRequest.class, name = "WorkersPerShiftRequest"),
    @JsonSubTypes.Type(value = ConsecutiveWorkingDaysRequest.class, name = "ConsecutiveWorkingDaysRequest")
})
public interface ConstraintRequest extends Serializable {
  ConstraintRequestId getId();
}
