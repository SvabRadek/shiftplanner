package com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction;


import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.cocroachden.planner.solver.service.schedule.ScheduleDay;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;

import java.time.LocalDate;
import java.util.Map;

public class ShiftFollowUpConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest) {
    var request = (ShiftFollowUpRestrictionRequest) constraintRequest;
    var owner = request.getOwner();
    if (owner.isEmpty()) {
      schedulePlan.getAssignments().forEach((employeeId, assignments) -> {
        this.applyConstraint(model, objective, assignments, request, schedulePlan.getWeightForEmployee(employeeId));
      });
    } else {
      var assignments = schedulePlan.getAssignments().get(owner.get());
      this.applyConstraint(model, objective, assignments, request, schedulePlan.getWeightForEmployee(owner.get()));
    }
  }

  private void applyConstraint(CpModel model, SolutionObjectives objective, Map<LocalDate, ScheduleDay> assignments, ShiftFollowUpRestrictionRequest request, Integer weight) {
    assignments.forEach((date, workDay) -> {
      var shift = workDay.getShifts(request.getFirstShift()).get(0);
      var nextDay = workDay.date().plusDays(1);
      if (assignments.containsKey(nextDay)) {
        var forbiddenFollowUp = assignments.get(nextDay).getShifts(request.getForbiddenFollowup()).get(0);
        var sum = LinearExpr.newBuilder().add(shift).add(forbiddenFollowUp);
        if (request.getPenalty() == 0) {
          model.addLessOrEqual(sum, 1);
        } else {
          var excess = model.newIntVar(0, 1, "");
          var zero = model.newConstant(0);
          var expr = LinearExpr.newBuilder().add(sum).add(-1);
          model.addMaxEquality(excess, new LinearArgument[]{ expr, zero });
          objective.addIntCost(excess, request.getPenalty() * weight);
        }
      }
    });
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof ShiftFollowUpRestrictionRequest;
  }
}
