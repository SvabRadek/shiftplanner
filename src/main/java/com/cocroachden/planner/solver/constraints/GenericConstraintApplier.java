package com.cocroachden.planner.solver.constraints;

import com.cockroachden.shiftschedulerback.model.schedule.Objectives;
import com.cockroachden.shiftschedulerback.model.schedule.SchedulePlan;
import com.google.ortools.sat.CpModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class GenericConstraintApplier {
  private final List<Constraint> constraintList;

  public GenericConstraintApplier(Constraint... constraintList) {
    this.constraintList = Arrays.stream(constraintList).toList();
  }

  public void apply(
      SchedulePlan schedulePlan,
      CpModel model,
      Objectives objectives,
      ConstraintRequest request
  ) {
      log.debug("Applying '{}'", request.getId());
      this.getSupportingConstraint(request).apply(schedulePlan, model, objectives, request);
  }

  private Constraint getSupportingConstraint(ConstraintRequest request) {
    var supportingConstraints = this.constraintList.stream()
        .filter(constraint -> constraint.supports(request))
        .toList();
    if (supportingConstraints.isEmpty()) {
      throw new RuntimeException("Request '%s' is not supported by any constraint.".formatted(request.getId()));
    }
    if (supportingConstraints.size() > 1) {
      throw new RuntimeException("Request '%s' is supported by multiple constraints. That is not allowed.".formatted(request.getId()));
    }
    return supportingConstraints.get(0);
  }

}
