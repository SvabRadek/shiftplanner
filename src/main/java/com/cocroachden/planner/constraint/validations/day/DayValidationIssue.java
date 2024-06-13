package com.cocroachden.planner.constraint.validations.day;

import com.cocroachden.planner.constraint.validations.IssueSeverity;
import com.cocroachden.planner.core.StupidDate;
import dev.hilla.Nonnull;

import java.io.Serializable;

public record DayValidationIssue(
    @Nonnull StupidDate localDate,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
