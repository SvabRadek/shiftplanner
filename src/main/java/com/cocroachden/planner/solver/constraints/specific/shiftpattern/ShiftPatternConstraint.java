package com.cocroachden.planner.solver.constraints.specific.shiftpattern;


import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
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

public class ShiftPatternConstraint implements Constraint {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    var request = (ShiftPatternConstraintRequest) constraintRequest;
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
      ShiftPatternConstraintRequest request,
      Integer weight
  ) {
    var pattern = request.getShiftPattern();
    var patternSize = request.getShiftPattern().length;
    var startIndex = request.getStartDateIndex();

    var workDays = assignments.values().stream().toList();
    for (int i = 0; i < assignments.size(); i++) {
      var adjustedIndex = (i + startIndex) % patternSize;
      var shiftForGivenDay = pattern[adjustedIndex];
      workDays.get(adjustedIndex).getShifts(shiftForGivenDay)
          .forEach(s -> objective.addBoolCost(s, (request.getReward() * -1) * weight));
    }
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof ShiftPatternConstraintRequest;
  }
}
