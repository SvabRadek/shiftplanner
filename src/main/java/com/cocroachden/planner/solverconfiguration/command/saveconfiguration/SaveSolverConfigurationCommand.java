package com.cocroachden.planner.solverconfiguration.command.saveconfiguration;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.constraint.ConstraintDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationDTO;

import java.time.LocalDate;
import java.util.List;

public record SaveSolverConfigurationCommand(
        SolverConfigurationId id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        List<EmployeeAssignmentDTO> assignedEmployees,
        List<ConstraintDTO> constraints
) implements Command {

    public static SaveSolverConfigurationCommand from(SolverConfigurationDTO dto) {
        return new SaveSolverConfigurationCommand(
                new SolverConfigurationId(dto.getId()),
                dto.getName(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getEmployees(),
                dto.getConstraints()
        );
    }

}
