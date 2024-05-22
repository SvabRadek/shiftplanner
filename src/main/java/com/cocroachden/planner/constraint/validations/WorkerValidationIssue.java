package com.cocroachden.planner.constraint.validations;

import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.validator.IssueSeverity;
import dev.hilla.Nonnull;

import java.io.Serializable;

public record WorkerValidationIssue(
    @Nonnull WorkerId workerId,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
