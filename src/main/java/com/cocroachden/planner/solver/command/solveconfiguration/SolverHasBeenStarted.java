package com.cocroachden.planner.solver.command.solveconfiguration;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solver.SolverSubscriptionId;

public record SolverHasBeenStarted(
        SolverSubscriptionId subscriptionId
) implements Event {
}
