package com.cocroachden.planner.solverconfiguration.service;

import com.cocroachden.planner.constraint.ConstraintId;
import com.cocroachden.planner.constraint.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.mapping.ConstraintMapper;
import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRepository;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.DeleteSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.SolverConfigurationHasBeenDeleted;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SolverConfigurationHasBeenSaved;
import com.cocroachden.planner.solverconfiguration.command.updateconfiguration.SolverConfigurationHasBeenUpdated;
import com.cocroachden.planner.solverconfiguration.command.updateconfiguration.UpdateSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRepository;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class SolverConfigurationService {
    private final EmployeeAssignmentRepository assignmentRepository;
    private final SolverConfigurationRepository configurationRepository;
    private final EmployeeRepository employeeRepository;
    private final ConstraintRepository constraintRepository;

    @EventListener
    public SolverConfigurationHasBeenSaved handle(SaveSolverConfigurationCommand command) {
        log.debug("Handling SaveSolverConfigurationCommand");
        var id = command.id();
        if (configurationRepository.existsById(id.getId())) {
            throw new IllegalArgumentException("Solver configuration with employeeId [" + id + "] already exists!");
        }
        var savedConfig = this.saveConfiguration(
                command.id(),
                command.name(),
                command.startDate(),
                command.endDate(),
                command.assignedEmployees(),
                command.constraints()
        );
        return new SolverConfigurationHasBeenSaved(savedConfig);
    }

    @EventListener
    public SolverConfigurationHasBeenUpdated handle(UpdateSolverConfigurationCommand command) {
        log.debug("Handling UpdateSolverConfigurationCommand");
        if (!configurationRepository.existsById(command.id().getId())) {
            throw new IllegalArgumentException("Solver configuration with employeeId [" + command.id() + "] does not exists!");
        }
        var savedConfig = this.saveConfiguration(
                command.id(),
                command.name(),
                command.startDate(),
                command.endDate(),
                command.assignedEmployees(),
                command.constraints()
        );
        return new SolverConfigurationHasBeenUpdated(savedConfig);
    }

    @EventListener
    public SolverConfigurationHasBeenDeleted handle(DeleteSolverConfigurationCommand command) {
        log.debug("Handling DeleteSolverConfigurationCommand");
        if (!configurationRepository.existsById(command.configurationId().getId())) {
            return null;
        }
        var config = configurationRepository.findById(command.configurationId().getId()).orElseThrow();

        var assignments = config.getEmployeeAssignments();
        assignments.forEach(assignment -> {
            assignment.getEmployee().getAssignments().remove(assignment);
        });
        assignments.clear();
        assignmentRepository.deleteAll(assignments);

        var constraints = config.getConstraintRecords();
        constraints.forEach(constraint -> {
            if (constraint.getOwner() != null) {
                constraint.getOwner().getConstraints().remove(constraint);
            }
        });
        constraints.clear();
        constraintRepository.deleteAll(constraints);
        configurationRepository.delete(config);
        return new SolverConfigurationHasBeenDeleted(command.configurationId());
    }

    protected SolverConfigurationRecord saveConfiguration(
            SolverConfigurationId id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            List<EmployeeAssignmentDTO> assignedEmployees,
            List<ConstraintRequestDTO> constraints
    ) {
        var configRecord = configurationRepository.save(
                new SolverConfigurationRecord()
                        .setId(id.getId())
                        .setName(name)
                        .setStartDate(startDate)
                        .setEndDate(endDate)
        );
        var assignedEmployeeRecords = assignedEmployees.stream()
                .map(assignment -> {
                    var employeeId = EmployeeId.from(assignment.getEmployeeId());
                    var employeeRecord = employeeRepository.findById(employeeId.getId()).orElseThrow();
                    return new EmployeeAssignmentRecord()
                            .setIndex(assignment.getIndex())
                            .setWeight(assignment.getWeight())
                            .setEmployee(employeeRecord)
                            .setConfiguration(configRecord);
                }).toList();
        var constraintRecords = constraints.stream()
                .map(constraintDto -> {
                    var constraint = ConstraintMapper.fromDto(constraintDto);
                    var constraintRecord = new ConstraintRecord()
                            .setId(constraintDto.getId())
                            .setRequest(constraint)
                            .setParent(configRecord);
                    if (constraint instanceof EmployeeConstraint employeeConstraint) {
                        employeeRepository.findById(employeeConstraint.getOwner().getId())
                                .ifPresent(constraintRecord::setOwner);
                    }
                    return constraintRecord;
                }).toList();
        assignmentRepository.saveAll(assignedEmployeeRecords);
        constraintRepository.saveAll(constraintRecords);
        return configurationRepository.save(configRecord);
    }
}
