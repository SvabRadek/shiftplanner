package com.cocroachden.planner.solver.command.stopsolver;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solver.SolverSubscriptionId;

public record SolverHasBeenStopped(
        SolverSubscriptionId subscriptionId
) implements Event {
}
