package com.cocroachden.planner.solver.command.saveconfiguration;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;

public record SolverConfigurationHasBeenSaved(
        SolverConfigurationRecord record
) implements Event {
}
