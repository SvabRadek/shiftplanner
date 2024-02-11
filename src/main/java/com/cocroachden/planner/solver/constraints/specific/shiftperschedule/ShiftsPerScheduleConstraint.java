package com.cocroachden.planner.solver.constraints.specific.shiftperschedule;


import com.cocroachden.planner.solver.constraints.Constraint;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.MinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlan;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;

import java.util.ArrayList;

public class ShiftsPerScheduleConstraint implements Constraint {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    var request = (ShiftsPerScheduleRequest) constraintRequest;
    var targetShift = request.getTargetShift();
    var workDays = schedulePlan.getAllDaysForWorker(request.getOwner().orElseThrow());
    var assignedShifts = new ArrayList<BoolVar>();
    workDays.forEach(workDay -> assignedShifts.addAll(workDay.getShifts(targetShift)));
    var asArray = assignedShifts.toArray(BoolVar[]::new);
    MinMaxConstraint.apply(
        request,
        asArray,
        workDays.size(),
        model,
        objective,
        schedulePlan.getWeightForWorker(request.getOwner().get())
    );
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof ShiftsPerScheduleRequest;
  }
}
