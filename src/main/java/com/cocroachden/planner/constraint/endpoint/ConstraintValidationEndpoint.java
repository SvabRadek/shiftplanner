package com.cocroachden.planner.constraint.endpoint;

import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.validations.ConstraintDayValidator;
import com.cocroachden.planner.constraint.validations.ConstraintWorkerValidator;
import com.cocroachden.planner.constraint.validations.DayValidationIssue;
import com.cocroachden.planner.constraint.validations.WorkerValidationIssue;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;

import java.util.List;

@AnonymousAllowed
@BrowserCallable
public class ConstraintValidationEndpoint {

  public @Nonnull List<@Nonnull DayValidationIssue> validateDays(
      @Nonnull PlannerConfigurationDTO configuration,
      @Nonnull List<@Nonnull ConstraintRequestDTO> constraints
  ) {
    return ConstraintDayValidator.validate(configuration, constraints);
  }

  public @Nonnull List<@Nonnull WorkerValidationIssue> validateWorkers(
      @Nonnull PlannerConfigurationDTO configuration,
      @Nonnull List<@Nonnull ConstraintRequestDTO> constraints
  ) {
    return ConstraintWorkerValidator.validate(configuration, constraints);
  }
}
