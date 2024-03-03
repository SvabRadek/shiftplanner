package com.cocroachden.planner.solver.constraints.validator;

import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ValidatorIssue {
  private @Nonnull String owner;
  private @Nonnull IssueSeverity severity;
  private @Nonnull String issue;
}
