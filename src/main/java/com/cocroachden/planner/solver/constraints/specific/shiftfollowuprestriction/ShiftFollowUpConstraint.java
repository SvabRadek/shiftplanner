package com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction;


import com.cocroachden.planner.solver.constraints.Constraint;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.request.ShiftFollowUpRestrictionRequest;
import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlan;
import com.cocroachden.planner.solver.schedule.WorkDay;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;

import java.time.LocalDate;
import java.util.Map;

public class ShiftFollowUpConstraint implements Constraint {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    var request = (ShiftFollowUpRestrictionRequest) constraintRequest;
    var owner = request.getOwner();
    if (owner.isEmpty()) {
      schedulePlan.assignments().forEach((workerId, assignments) -> {
        this.applyConstraint(model, objective, assignments, request, schedulePlan.getWeightForWorker(workerId));
      });
    } else {
      var assignments = schedulePlan.assignments().get(owner.get());
      this.applyConstraint(model, objective, assignments, request, schedulePlan.getWeightForWorker(owner.get()));
    }
  }

  private void applyConstraint(CpModel model, Objectives objective, Map<LocalDate, WorkDay> assignments, ShiftFollowUpRestrictionRequest request, Integer weight) {
    assignments.forEach((date, workDay) -> {
      var shift = workDay.getShift(request.getFirstShift());
      var nextDay = workDay.date().plusDays(1);
      if (assignments.containsKey(nextDay)) {
        var forbiddenFollowUp = assignments.get(nextDay).getShift(request.getForbiddenFollowup());
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
