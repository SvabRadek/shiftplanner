package com.cocroachden.planner.solver.command.solveconfiguration;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;

public record StartSolverCommand(
        SolverConfigurationId configurationId,
        SolverSubscriptionId subscriptionId,
        Integer limitInSec
) implements Command {
}
