package com.cocroachden.planner.solver.constraints;

import com.cocroachden.planner.constraint.api.*;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxRequest;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.EmployeeShiftRequest;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftRequest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "class_type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmployeeShiftRequest.class, name = "EmployeeShiftRequest"),
    @JsonSubTypes.Type(value = OneShiftPerDayRequest.class, name = "OneShiftPerDayRequest"),
    @JsonSubTypes.Type(value = ShiftFollowUpRestrictionRequest.class, name = "ShiftFollowUpRestrictionRequest"),
    @JsonSubTypes.Type(value = ShiftPatternConstraintRequest.class, name = "ShiftPatternConstraintRequest"),
    @JsonSubTypes.Type(value = ShiftsPerScheduleRequest.class, name = "ShiftsPerScheduleRequest"),
    @JsonSubTypes.Type(value = EmployeesPerShiftRequest.class, name = "EmployeesPerShiftRequest"),
    @JsonSubTypes.Type(value = AbstractEmployeeSpecificConstraint.class, name = "AbstractEmployeeSpecificConstraint"),
    @JsonSubTypes.Type(value = AbstractMinMaxRequest.class, name = "AbstractMinMaxRequest"),
    @JsonSubTypes.Type(value = ConsecutiveWorkingDaysRequest.class, name = "ConsecutiveWorkingDaysRequest"),
    @JsonSubTypes.Type(value = TripleShiftConstraintRequest.class, name = "TripleShiftConstraintRequest")
})
public interface ConstraintRequest extends Serializable {
  ConstraintType getType();

  String toString();
}
