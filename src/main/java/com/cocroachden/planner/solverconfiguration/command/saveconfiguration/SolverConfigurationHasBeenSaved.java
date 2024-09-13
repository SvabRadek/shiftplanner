package com.cocroachden.planner.solverconfiguration.command.saveconfiguration;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;

public record SolverConfigurationHasBeenSaved(
        SolverConfigurationRecord record
) implements Event {
}
