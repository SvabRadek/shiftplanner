package com.cocroachden.planner.solverconfiguration.validations.day;

import com.cocroachden.planner.solverconfiguration.validations.IssueSeverity;
import dev.hilla.Nonnull;

import java.io.Serializable;
import java.time.LocalDate;

public record DayValidationIssueDTO(
    @Nonnull LocalDate localDate,
    @Nonnull IssueSeverity severity,
    @Nonnull String issue
) implements Serializable {
}
