package com.cocroachden.planner.solver.constraints;


import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;

public interface ConstraintApplier {
  void apply(SchedulePlan schedulePlan, CpModel model, SolutionObjectives objective, ConstraintRequest constraintRequest);

  boolean supports(ConstraintRequest request);
}
