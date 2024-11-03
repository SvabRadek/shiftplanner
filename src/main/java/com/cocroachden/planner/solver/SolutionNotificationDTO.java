package com.cocroachden.planner.solver;

import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SolutionNotificationDTO {
    private @Nonnull SolutionStatus solutionStatus;
    private @Nonnull String subscriptionId;
}
