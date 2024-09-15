package com.cocroachden.planner.solver.constraints.specific.employeespershift;

import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.MinMaxConstraint;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftConstraint;
import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.google.ortools.sat.BoolVar;
import com.google.ortools.sat.CpModel;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EmployeesPerShiftConstraintApplier implements ConstraintApplier {
  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
    var request = (EmployeesPerShiftConstraint) solverConstraint;
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
  public boolean supports(SolverConstraint request) {
    return request instanceof EmployeesPerShiftConstraint;
  }
}
