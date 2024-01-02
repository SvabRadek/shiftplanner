package com.cocroachden.planner.solver.constraints;


import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;

public interface Constraint {
  void apply(SchedulePlan schedulePlan, CpModel model, Objectives objective, ConstraintRequest constraintRequest);

  boolean supports(ConstraintRequest request);
}
