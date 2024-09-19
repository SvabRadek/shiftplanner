package com.cocroachden.planner.solverconfiguration.endpoint;

import com.cocroachden.planner.security.Role;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;
import com.cocroachden.planner.solverconfiguration.validations.day.ConstraintDayValidator;
import com.cocroachden.planner.solverconfiguration.validations.day.DayValidationIssueDTO;
import com.cocroachden.planner.solverconfiguration.validations.employee.ConstraintEmployeeValidator;
import com.cocroachden.planner.solverconfiguration.validations.employee.EmployeeValidationIssueDTO;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@BrowserCallable
@RolesAllowed(Role.ROLE_USER)
public class SolverConfigurationValidationEndpoint {

  public @Nonnull List<@Nonnull DayValidationIssueDTO> validateDays(@Nonnull SolverConfigurationDTO configuration) {
    return ConstraintDayValidator.validate(configuration);
  }

  public @Nonnull List<@Nonnull EmployeeValidationIssueDTO> validateEmployees(@Nonnull SolverConfigurationDTO configuration) {
    return ConstraintEmployeeValidator.validate(configuration);
  }
}
