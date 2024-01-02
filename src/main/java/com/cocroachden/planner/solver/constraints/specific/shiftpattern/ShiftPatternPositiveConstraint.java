package com.cocroachden.planner.solver.constraints.specific.shiftpattern;


import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternPositiveConstraintRequest;
import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlan;
import com.cocroachden.planner.solver.schedule.WorkDay;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearArgument;
import com.google.ortools.sat.LinearExpr;
import com.cocroachden.planner.solver.constraints.Constraint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class ShiftPatternPositiveConstraint implements Constraint {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    var request = (ShiftPatternPositiveConstraintRequest) constraintRequest;
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

  private void applyConstraint(
      CpModel model,
      Objectives objective,
      Map<LocalDate, WorkDay> assignments,
      ShiftPatternPositiveConstraintRequest request,
      Integer weight
  ) {
    var pattern = request.getShiftPattern();
    var patternSize = request.getShiftPattern().length;

    assignments.forEach((date, workDay) -> {
      var shifts = new ArrayList<BoolVar>();
      for (int i = 0; i < patternSize; i++) {
        var patternDate = date.plusDays(i);
        if (assignments.containsKey(patternDate)) {
          shifts.add(assignments.get(patternDate).getShift(pattern[i]));
        }
      }
      var patternHits = model.newIntVar(0, patternSize, "");
      model.addEquality(patternHits, LinearExpr.sum(shifts.toArray(new LinearArgument[]{})));
      var expr = LinearExpr.newBuilder().add(patternHits).addTerm(model.newConstant(patternSize), -1).add(1);
      var hit = model.newIntVar(0, 1, "");
      model.addMaxEquality(hit, new LinearArgument[]{ expr, model.newConstant(0) });
      objective.addIntCost(hit, request.getReward() * -1 * weight);
    });
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof ShiftPatternPositiveConstraintRequest;
  }
}
