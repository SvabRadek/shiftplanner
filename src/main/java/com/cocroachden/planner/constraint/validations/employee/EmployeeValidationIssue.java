package com.cocroachden.planner.constraint.validations.employee;

import com.cocroachden.planner.constraint.validations.IssueSeverity;
import dev.hilla.Nonnull;

import java.io.Serializable;

public record EmployeeValidationIssue(
    @Nonnull String employeeId,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
