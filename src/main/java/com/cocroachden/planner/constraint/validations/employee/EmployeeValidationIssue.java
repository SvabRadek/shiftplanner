package com.cocroachden.planner.constraint.validations.employee;

import com.cocroachden.planner.constraint.validations.IssueSeverity;
import com.cocroachden.planner.employee.api.EmployeeId;
import dev.hilla.Nonnull;

import java.io.Serializable;

public record EmployeeValidationIssue(
    @Nonnull Long employeeId,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
