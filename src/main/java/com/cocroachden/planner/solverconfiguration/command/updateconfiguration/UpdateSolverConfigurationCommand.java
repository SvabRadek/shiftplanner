package com.cocroachden.planner.solverconfiguration.command.updateconfiguration;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.constraint.ConstraintDTO;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;

import java.time.LocalDate;
import java.util.List;

public record UpdateSolverConfigurationCommand(
        SolverConfigurationId id,
        String name,
        String username,
        LocalDate startDate,
        LocalDate endDate,
        List<EmployeeAssignmentDTO> assignedEmployees,
        List<ConstraintDTO> constraints
) implements Command {
}
