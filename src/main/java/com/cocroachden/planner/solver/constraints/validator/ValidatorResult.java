package com.cocroachden.planner.solver.constraints.validator;

import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ValidatorResult {
  public static ValidatorResult empty() {
    return new ValidatorResult(new ArrayList<>());
  }

  @Nonnull
  private List<@Nonnull ValidatorIssue> issues;

  public ValidatorResult merge(ValidatorResult validatorResult) {
    var mergedIssues = new ArrayList<>(issues);
    mergedIssues.addAll(validatorResult.getIssues());
    return new ValidatorResult(mergedIssues);
  }
}
