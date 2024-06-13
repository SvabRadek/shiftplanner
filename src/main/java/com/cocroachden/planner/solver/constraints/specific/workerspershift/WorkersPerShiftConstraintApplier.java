package com.cocroachden.planner.solver.constraints.specific.workerspershift;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.MinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.request.WorkersPerShiftRequest;
import com.cocroachden.planner.solver.solver.Objectives;
import com.cocroachden.planner.solver.solver.schedule.SchedulePlan;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;

import java.util.Collection;

public class WorkersPerShiftConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest) {
    var request = (WorkersPerShiftRequest) constraintRequest;
    var assignmentsByDate = schedulePlan.getAllAssignmentsByDate();
    assignmentsByDate.forEach((date, workDays) -> {
      var shiftAssignmentsInOneDay = workDays.stream()
          .map(workDay -> workDay.getShifts(request.getShift()))
          .flatMap(Collection::stream)
          .toArray(BoolVar[]::new);
      MinMaxConstraint.apply(
          request,
          shiftAssignmentsInOneDay,
          schedulePlan.getAssignments().size(),
          model,
          objective,
          1
      );
    });
  }

  @Override
  public boolean supports(ConstraintRequest request) {
    return request instanceof WorkersPerShiftRequest;
  }
}
