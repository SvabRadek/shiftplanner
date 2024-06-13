package com.cocroachden.planner.solver.constraints.specific.employeespershift;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.specific.MinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftRequest;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;

import java.util.Collection;

public class EmployeesPerShiftConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest) {
    var request = (EmployeesPerShiftRequest) constraintRequest;
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
    return request instanceof EmployeesPerShiftRequest;
  }
}
