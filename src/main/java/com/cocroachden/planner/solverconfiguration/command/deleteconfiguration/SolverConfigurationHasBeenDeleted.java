package com.cocroachden.planner.solverconfiguration.command.deleteconfiguration;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;

public record SolverConfigurationHasBeenDeleted(
        SolverConfigurationId configurationId
) implements Event {
}
