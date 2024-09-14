package com.cocroachden.planner.solver.constraints.specific.shiftperschedule;


import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.MinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleRequest;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class ShiftsPerScheduleConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest) {
    var request = (ShiftsPerScheduleRequest) constraintRequest;
    var targetShift = request.getTargetShift();
    var workDays = schedulePlan.getAllDaysForEmployee(request.getOwner());
    var assignedShifts = new ArrayList<BoolVar>();
    workDays.forEach(workDay -> assignedShifts.addAll(workDay.getShifts(targetShift)));
    var asArray = assignedShifts.toArray(BoolVar[]::new);
    MinMaxConstraint.apply(
        request,
        asArray,
        workDays.size(),
        model,
        objective,
        schedulePlan.getWeightForEmployee(request.getOwner())
    );
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof ShiftsPerScheduleRequest;
  }
}
