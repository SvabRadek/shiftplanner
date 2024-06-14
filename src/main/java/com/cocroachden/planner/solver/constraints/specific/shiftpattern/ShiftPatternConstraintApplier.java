package com.cocroachden.planner.solver.constraints.specific.shiftpattern;


import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.request.ShiftPatternConstraintRequest;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.cocroachden.planner.solver.service.schedule.ScheduleDay;
import com.google.ortools.sat.CpModel;
import com.cocroachden.planner.solver.constraints.ConstraintApplier;

import java.time.LocalDate;
import java.util.Map;

public class ShiftPatternConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest) {
    var request = (ShiftPatternConstraintRequest) constraintRequest;
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

  private void applyConstraint(
      CpModel model,
      SolutionObjectives objective,
      Map<LocalDate, ScheduleDay> assignments,
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
