package com.cocroachden.planner.constraint.validations.worker;

import com.cocroachden.planner.constraint.validations.IssueSeverity;
import com.cocroachden.planner.core.identity.WorkerId;
import dev.hilla.Nonnull;

import java.io.Serializable;

public record WorkerValidationIssue(
    @Nonnull WorkerId workerId,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
