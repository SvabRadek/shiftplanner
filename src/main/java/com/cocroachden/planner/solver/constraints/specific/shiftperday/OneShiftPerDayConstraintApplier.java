package com.cocroachden.planner.solver.constraints.specific.shiftperday;


import com.cocroachden.planner.solver.constraints.ConstraintApplier;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayConstraint;
import com.cocroachden.planner.solver.solver.solution.SolutionObjectives;
import com.cocroachden.planner.solver.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.LinearExpr;
import org.springframework.stereotype.Service;

@Service
public class OneShiftPerDayConstraintApplier implements ConstraintApplier {

  @Override
  public void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint) {
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
  public boolean supports(SolverConstraint request) {
    return request instanceof OneShiftPerDayConstraint;
  }
}
