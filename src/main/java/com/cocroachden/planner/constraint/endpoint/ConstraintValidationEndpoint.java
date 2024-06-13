package com.cocroachden.planner.constraint.endpoint;

import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.validations.day.ConstraintDayValidator;
import com.cocroachden.planner.constraint.validations.worker.ConstraintWorkerValidator;
import com.cocroachden.planner.constraint.validations.day.DayValidationIssue;
import com.cocroachden.planner.constraint.validations.worker.WorkerValidationIssue;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;

import java.util.List;

@AnonymousAllowed
@BrowserCallable
public class ConstraintValidationEndpoint {

  public @Nonnull List<@Nonnull DayValidationIssue> validateDays(
      @Nonnull SolverConfigurationDTO configuration,
      @Nonnull List<@Nonnull ConstraintRequestDTO> constraints
  ) {
    return ConstraintDayValidator.validate(configuration, constraints);
  }

  public @Nonnull List<@Nonnull WorkerValidationIssue> validateWorkers(
      @Nonnull SolverConfigurationDTO configuration,
      @Nonnull List<@Nonnull ConstraintRequestDTO> constraints
  ) {
    return ConstraintWorkerValidator.validate(configuration, constraints);
  }
}
