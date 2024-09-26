package com.cocroachden.planner.solver.command.solveconfiguration;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;

public record StartSolverCommand(
        SolverConfigurationDTO solverConfiguration,
        SolverSubscriptionId subscriptionId,
        Integer limitInSec,
        String username
) implements Command {
}
