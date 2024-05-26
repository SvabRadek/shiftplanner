package com.cocroachden.planner.constraint.validations;

import com.cocroachden.planner.lib.StupidDate;
import dev.hilla.Nonnull;

import java.io.Serializable;

public record DayValidationIssue(
    @Nonnull StupidDate localDate,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
