package com.cocroachden.planner.solver.constraints;


import com.cocroachden.planner.solver.service.solver.SolutionObjectives;
import com.cocroachden.planner.solver.service.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;

public interface ConstraintApplier {
  void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, SolverConstraint solverConstraint);

  boolean supports(SolverConstraint request);
}
