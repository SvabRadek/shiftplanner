package com.cocroachden.planner.solver.constraints.validator;

import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import lombok.AllArgsConstructor;

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
