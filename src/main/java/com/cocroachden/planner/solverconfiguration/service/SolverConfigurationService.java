package com.cocroachden.planner.solverconfiguration.service;

import com.cocroachden.planner.constraint.ConstraintDTO;
import com.cocroachden.planner.constraint.mapping.ConstraintMapper;
import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.solver.constraints.specific.EmployeeConstraint;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.DeleteSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.SolverConfigurationHasBeenDeleted;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SolverConfigurationHasBeenSaved;
import com.cocroachden.planner.solverconfiguration.command.updateconfiguration.SolverConfigurationHasBeenUpdated;
import com.cocroachden.planner.solverconfiguration.command.updateconfiguration.UpdateSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SolverConfigurationService {
    private final SolverConfigurationRepository configurationRepository;
    private final EntityManager entityManager;

    @EventListener
    @Transactional
    public SolverConfigurationHasBeenSaved handle(SaveSolverConfigurationCommand command) {
        log.debug("Handling SaveSolverConfigurationCommand...");
        if (configurationRepository.existsById(command.id().getId())) {
            throw new IllegalArgumentException("Solver configuration with id [" + command
                    .id()
                    .getId() + "] already exists!");
        }
        var configurationRecord = new SolverConfigurationRecord()
                .setId(command.id().getId())
                .setOwningUser(command.username())
                .setName(command.name())
                .setStartDate(command.startDate())
                .setEndDate(command.endDate());
        this.addAssignmentsAndConstraints(
                configurationRecord,
                command.assignedEmployees(),
                command.constraints()
        );
        configurationRepository.save(configurationRecord);
        return new SolverConfigurationHasBeenSaved(command.id());
    }

    @EventListener
    @Transactional
    public SolverConfigurationHasBeenUpdated handle(UpdateSolverConfigurationCommand command) throws IllegalAccessException {
        log.debug("Handling UpdateSolverConfigurationCommand...");
        if (configurationRepository.existsById(command.id().getId())) {
            configurationRepository.deleteById(command.id().getId());
        }
        this.handle(
                new SaveSolverConfigurationCommand(
                        command.id(),
                        command.name(),
                        command.username(),
                        command.startDate(),
                        command.endDate(),
                        command.assignedEmployees(),
                        command.constraints()
                )
        );
        return new SolverConfigurationHasBeenUpdated(command.id());
    }

    @EventListener
    @Transactional
    public SolverConfigurationHasBeenDeleted handle(DeleteSolverConfigurationCommand command) {
        log.debug("Handling DeleteSolverConfigurationCommand...");
        return configurationRepository.findByIdAndOwningUser(
                command.configurationId().getId(),
                command.username()
        ).map(existing -> {
            configurationRepository.delete(existing);
            return new SolverConfigurationHasBeenDeleted(command.configurationId());
        }).orElse(null);
    }

    private void addAssignmentsAndConstraints(
            SolverConfigurationRecord solverConfiguration,
            List<EmployeeAssignmentDTO> assignments,
            List<ConstraintDTO> constraints
    ) {
        var cachedEmployees = new HashMap<EmployeeId, EmployeeRecord>();
        assignments.forEach(assignment -> {
            var employeeId = assignment.getEmployeeId();
            var employeeRecord = cachedEmployees.computeIfAbsent(employeeId, this::fetchEmployeeReference);
            new EmployeeAssignmentRecord()
                    .setIndex(assignment.getIndex())
                    .setWeight(assignment.getWeight())
                    .setEmployee(employeeRecord)
                    .setConfiguration(solverConfiguration);
        });
        constraints.forEach(constraintDto -> {
            var constraint = ConstraintMapper.fromDto(constraintDto);
            var constraintRecord = new ConstraintRecord()
                    .setId(constraintDto.getId())
                    .setRequest(constraint)
                    .setParent(solverConfiguration);
            if (constraint instanceof EmployeeConstraint employeeConstraint) {
                var employeeId = employeeConstraint.getOwner();
                var employeeRecord = cachedEmployees.computeIfAbsent(employeeId, this::fetchEmployeeReference);
                constraintRecord.setOwner(employeeRecord);
            }
        });
    }

    private EmployeeRecord fetchEmployeeReference(EmployeeId employeeId) {
        return entityManager.getReference(EmployeeRecord.class, employeeId.getId());
    }
}
