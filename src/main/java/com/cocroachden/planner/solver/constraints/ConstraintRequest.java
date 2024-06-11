package com.cocroachden.planner.solver.constraints;

import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.solver.constraints.specific.AbstractConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.request.SpecificShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.request.WorkersPerShiftRequest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "class_type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SpecificShiftRequest.class, name = "SpecificShiftRequest"),
    @JsonSubTypes.Type(value = OneShiftPerDayRequest.class, name = "OneShiftPerDayRequest"),
    @JsonSubTypes.Type(value = ShiftFollowUpRestrictionRequest.class, name = "ShiftFollowUpRestrictionRequest"),
    @JsonSubTypes.Type(value = ShiftPatternConstraintRequest.class, name = "ShiftPatternConstraintRequest"),
    @JsonSubTypes.Type(value = ShiftsPerScheduleRequest.class, name = "ShiftsPerScheduleRequest"),
    @JsonSubTypes.Type(value = WorkersPerShiftRequest.class, name = "WorkersPerShiftRequest"),
    @JsonSubTypes.Type(value = AbstractConstraintRequest.class, name = "AbstractConstraintRequest"),
    @JsonSubTypes.Type(value = AbstractMinMaxRequest.class, name = "AbstractMinMaxRequest"),
    @JsonSubTypes.Type(value = ConsecutiveWorkingDaysRequest.class, name = "ConsecutiveWorkingDaysRequest"),
    @JsonSubTypes.Type(value = TripleShiftConstraintRequest.class, name = "TripleShiftConstraintRequest")
})
public interface ConstraintRequest extends Serializable {
  ConstraintType getType();
  String toString();
}
