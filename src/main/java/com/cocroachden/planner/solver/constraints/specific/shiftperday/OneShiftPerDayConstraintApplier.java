package com.cocroachden.planner.solver.constraints.specific.shiftperday;


import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayRequest;
import com.cocroachden.planner.solver.solver.Objectives;
import com.cocroachden.planner.solver.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearExpr;

public class OneShiftPerDayConstraintApplier implements ConstraintApplier {

  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    schedulePlan.getAssignments().values().forEach(assignments -> {
      assignments.values().forEach(workDay -> {
        var shiftsInADay = LinearExpr.newBuilder()
            .add(workDay.offShiftAssignment())
            .add(workDay.dayShiftAssignment())
            .add(workDay.nightShiftAssignment());
        model.addEquality(shiftsInADay, 1);
      });
    });
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof OneShiftPerDayRequest;
  }
}
