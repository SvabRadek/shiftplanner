package com.cocroachden.planner.solver.command.deleteconfiguration;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solver.SolverConfigurationId;

public record SolverConfigurationHasBeenDeleted(
        SolverConfigurationId configurationId
) implements Event {
}
