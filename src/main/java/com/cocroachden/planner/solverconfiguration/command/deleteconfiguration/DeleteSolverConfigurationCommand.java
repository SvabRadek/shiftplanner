package com.cocroachden.planner.solverconfiguration.command.deleteconfiguration;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;

public record DeleteSolverConfigurationCommand(
        SolverConfigurationId configurationId,
        String username
) implements Command {
}
