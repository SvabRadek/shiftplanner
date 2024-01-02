package com.cocroachden.planner.solver.constraints.specific.shiftperschedule;


import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.MinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlan;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import com.cocroachden.planner.solver.constraints.Constraint;

import java.util.ArrayList;

public class ShiftsPerScheduleConstraint implements Constraint {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    var request = (ShiftsPerScheduleRequest) constraintRequest;
    var targetShift = request.getTargetShift();
    var workDays = schedulePlan.getAllDaysForWorker(request.getOwner().orElseThrow());
    var assignedShifts = new ArrayList<BoolVar>();
    workDays.forEach(workDay -> {
      if (targetShift.equals(WorkShifts.WORKING_SHIFTS)) {
        assignedShifts.add(workDay.getShift(WorkShifts.DAY));
        assignedShifts.add(workDay.getShift(WorkShifts.NIGHT));
      } else {
        assignedShifts.add(workDay.getShift(targetShift));
      }
    });
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
