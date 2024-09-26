package com.cocroachden.planner.solver;

import com.cocroachden.planner.solver.solver.Solver;

public record SolverTask(
        SolverSubscriptionId subscriptionId,
        Solver solver,
        String owningUser
) {
}
