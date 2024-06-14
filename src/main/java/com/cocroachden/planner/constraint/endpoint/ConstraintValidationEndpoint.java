package com.cocroachden.planner.constraint.endpoint;

import com.cocroachden.planner.constraint.validations.day.ConstraintDayValidator;
import com.cocroachden.planner.constraint.validations.day.DayValidationIssue;
import com.cocroachden.planner.constraint.validations.employee.ConstraintEmployeeValidator;
import com.cocroachden.planner.constraint.validations.employee.EmployeeValidationIssue;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;

import java.util.List;

@AnonymousAllowed
@BrowserCallable
public class ConstraintValidationEndpoint {

  public @Nonnull List<@Nonnull DayValidationIssue> validateDays(@Nonnull SolverConfigurationDTO configuration) {
    return ConstraintDayValidator.validate(configuration);
  }

  public @Nonnull List<@Nonnull EmployeeValidationIssue> validateEmployees(@Nonnull SolverConfigurationDTO configuration) {
    return ConstraintEmployeeValidator.validate(configuration);
  }
}
