package com.cocroachden.planner.solver.constraints;

import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.solver.constraints.specific.AbstractMinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.request.ConsecutiveWorkingDaysConstraint;
import com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.request.EvenShiftDistributionConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleConstraint;
import com.cocroachden.planner.solver.constraints.specific.teamassignment.request.TeamAssignmentConstraint;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.request.TripleShiftConstraint;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.request.RequestedShiftConstraint;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftConstraint;
import com.cocroachden.planner.solver.constraints.specific.weekends.request.WeekendConstraint;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "class_type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = RequestedShiftConstraint.class, name = "RequestedShiftConstraint"),
    @JsonSubTypes.Type(value = OneShiftPerDayConstraint.class, name = "OneShiftPerDayConstraint"),
    @JsonSubTypes.Type(value = ShiftFollowUpRestrictionConstraint.class, name = "ShiftFollowUpRestrictionConstraint"),
    @JsonSubTypes.Type(value = ShiftPatternConstraint.class, name = "ShiftPatternConstraint"),
    @JsonSubTypes.Type(value = ShiftsPerScheduleConstraint.class, name = "ShiftsPerScheduleConstraint"),
    @JsonSubTypes.Type(value = EmployeesPerShiftConstraint.class, name = "EmployeesPerShiftConstraint"),
    @JsonSubTypes.Type(value = AbstractMinMaxConstraint.class, name = "AbstractMinMaxConstraint"),
    @JsonSubTypes.Type(value = ConsecutiveWorkingDaysConstraint.class, name = "ConsecutiveWorkingDaysConstraint"),
    @JsonSubTypes.Type(value = TripleShiftConstraint.class, name = "TripleShiftConstraint"),
    @JsonSubTypes.Type(value = TeamAssignmentConstraint.class, name = "TeamAssignmentConstraint"),
    @JsonSubTypes.Type(value = WeekendConstraint.class, name = "WeekendConstraint"),
    @JsonSubTypes.Type(value = EvenShiftDistributionConstraint.class, name = "EvenShiftDistributionConstraint")
})
public interface SolverConstraint extends Serializable {
  ConstraintType getType();

  String toString();
}
