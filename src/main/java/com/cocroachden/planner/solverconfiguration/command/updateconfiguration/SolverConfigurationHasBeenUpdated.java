package com.cocroachden.planner.solverconfiguration.command.updateconfiguration;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationRecord;

public record SolverConfigurationHasBeenUpdated(
        SolverConfigurationRecord configurationRecord
) implements Event {
}
