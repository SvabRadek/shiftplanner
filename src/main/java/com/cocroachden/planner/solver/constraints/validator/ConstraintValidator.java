package com.cocroachden.planner.solver.constraints.validator;

import com.cocroachden.planner.constraint.ConstraintRequestDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.plannerconfiguration.repository.PlannerConfigurationRecord;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

@AllArgsConstructor
public class ConstraintValidator {

  private final List<SpecificConstraintValidator> specificConstraintValidators;

  public ValidatorResult validate(
      PlannerConfigurationDTO configurationRecord,
      List<ConstraintRequestDTO> constraints
  ) {
    return specificConstraintValidators.stream()
        .map(validator -> validator.validate(configurationRecord, constraints))
        .reduce(ValidatorResult::merge)
        .orElse(ValidatorResult.empty());
  }
}
