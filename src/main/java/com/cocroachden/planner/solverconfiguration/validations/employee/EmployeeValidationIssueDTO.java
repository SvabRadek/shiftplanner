package com.cocroachden.planner.solverconfiguration.validations.employee;

import com.cocroachden.planner.solverconfiguration.validations.IssueSeverity;
import dev.hilla.Nonnull;

import java.io.Serializable;

public record EmployeeValidationIssueDTO(
    @Nonnull String employeeId,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
