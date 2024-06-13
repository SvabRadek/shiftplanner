package com.cocroachden.planner.solver.constraints;


import com.cocroachden.planner.solver.solver.Objectives;
import com.cocroachden.planner.solver.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;

public interface ConstraintApplier {
  void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest);

  boolean supports(ConstraintRequest request);
}
