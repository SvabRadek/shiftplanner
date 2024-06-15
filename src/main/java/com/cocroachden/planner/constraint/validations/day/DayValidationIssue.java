package com.cocroachden.planner.constraint.validations.day;

import com.cocroachden.planner.constraint.validations.IssueSeverity;
import dev.hilla.Nonnull;

import java.io.Serializable;
import java.time.LocalDate;

public record DayValidationIssue(
    @Nonnull LocalDate localDate,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
