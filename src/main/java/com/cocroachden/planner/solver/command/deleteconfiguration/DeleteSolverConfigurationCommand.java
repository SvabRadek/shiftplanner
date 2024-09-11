package com.cocroachden.planner.solver.command.deleteconfiguration;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.solver.SolverConfigurationId;

public record DeleteSolverConfigurationCommand(
        SolverConfigurationId configurationId
) implements Command {
}
