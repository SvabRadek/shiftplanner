package com.cocroachden.planner.solver.constraints;


import com.cocroachden.planner.solver.solver.solution.SolutionObjectives;
import com.cocroachden.planner.solver.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;

public interface ConstraintApplier {
  void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint);

  boolean supports(SolverConstraint request);
}
