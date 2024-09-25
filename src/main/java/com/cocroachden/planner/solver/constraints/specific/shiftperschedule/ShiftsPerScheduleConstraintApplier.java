package com.cocroachden.planner.solver.constraints.specific.shiftperschedule;


import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.MinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.request.ShiftsPerScheduleConstraint;
import com.cocroachden.planner.solver.service.solver.SolutionObjectives;
import com.cocroachden.planner.solver.service.solver.schedule.SchedulePlan;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class ShiftsPerScheduleConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
    var request = (ShiftsPerScheduleConstraint) solverConstraint;
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
  public boolean supports(SolverConstraint request) {
    return request instanceof ShiftsPerScheduleConstraint;
  }
}
