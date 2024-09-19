package com.cocroachden.planner.solverconfiguration.command.saveconfiguration;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;

public record SolverConfigurationHasBeenSaved(
        SolverConfigurationId configurationId
) implements Event {
}
