package com.cocroachden.planner.solver.constraints;

import com.cocroachden.planner.solver.service.SolutionObjectives;
import com.cocroachden.planner.solver.service.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class GenericConstraintApplier {
  private final List<ConstraintApplier> constraintApplierList;

  public GenericConstraintApplier(ConstraintApplier... constraintApplierList) {
    this.constraintApplierList = Arrays.stream(constraintApplierList).toList();
  }

  public void apply(
      SchedulePlan schedulePlan,
      CpModel model,
      SolutionObjectives solutionObjectives,
      SolverConstraint request
  ) {
      this.getSupportingConstraint(request).apply(schedulePlan, model, solutionObjectives, request);
  }

  private ConstraintApplier getSupportingConstraint(SolverConstraint request) {
    var supportingConstraints = this.constraintApplierList.stream()
        .filter(constraint -> constraint.supports(request))
        .toList();
    if (supportingConstraints.isEmpty()) {
      throw new RuntimeException("Request '%s' is not supported by any constraint.".formatted(request.getType()));
    }
    if (supportingConstraints.size() > 1) {
      throw new RuntimeException("Request '%s' is supported by multiple constraints. That is not allowed.".formatted(request.getType()));
    }
    return supportingConstraints.get(0);
  }

}
