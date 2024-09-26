package com.cocroachden.planner.solver.command.stopsolver;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.solver.SolverSubscriptionId;

public record StopSolverCommand(
        SolverSubscriptionId subscriptionId,
        String username
) implements Command {
}
