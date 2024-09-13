package com.cocroachden.planner.constraint.command.saveconstraint;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;

public record SaveConstraintCommand(
        SolverConfigurationId solverConfigurationId,
        ConstraintRequestDTO constraint
) implements Command {
}
