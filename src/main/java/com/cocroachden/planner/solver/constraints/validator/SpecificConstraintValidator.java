package com.cocroachden.planner.solver.constraints.validator;

import com.cocroachden.planner.constraint.ConstraintRequestDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;

import java.util.List;

public interface SpecificConstraintValidator {
  ValidatorResult validate(
      PlannerConfigurationDTO configurationRecord,
      List<ConstraintRequestDTO> constraint
  );
}
