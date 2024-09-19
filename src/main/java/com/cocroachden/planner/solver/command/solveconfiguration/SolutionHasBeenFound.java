package com.cocroachden.planner.solver.command.solveconfiguration;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.SolverSubscriptionId;

public record SolutionHasBeenFound(
        SolverSubscriptionId subscriptionId,
        SolverSolutionDTO solution
) implements Event {
}
