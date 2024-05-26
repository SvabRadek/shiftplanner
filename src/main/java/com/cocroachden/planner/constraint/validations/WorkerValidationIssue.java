package com.cocroachden.planner.constraint.validations;

import com.cocroachden.planner.lib.WorkerId;
import dev.hilla.Nonnull;

import java.io.Serializable;

public record WorkerValidationIssue(
    @Nonnull WorkerId workerId,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
