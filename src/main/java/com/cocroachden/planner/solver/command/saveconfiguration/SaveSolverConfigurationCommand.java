package com.cocroachden.planner.solver.command.saveconfiguration;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.solver.api.SolverConfigurationDTO;

public record SaveSolverConfigurationCommand(
        SolverConfigurationDTO solverConfigurationDTO
) implements Command {
}
