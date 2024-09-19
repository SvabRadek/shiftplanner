package com.cocroachden.planner.solverconfiguration.command.updateconfiguration;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;

public record SolverConfigurationHasBeenUpdated(
        SolverConfigurationId configurationId
) implements Event {
}
