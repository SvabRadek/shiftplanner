package com.cocroachden.planner.solverconfiguration.service;

import com.cocroachden.planner.constraint.ConstraintId;
import com.cocroachden.planner.constraint.ConstraintRequestDTO;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.repository.ConstraintRequestRepository;
import com.cocroachden.planner.constraint.service.ConstraintMapper;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.constraints.specific.AbstractEmployeeSpecificConstraint;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
@Slf4j
public class SolverConfigurationService {
    private final EmployeeAssignmentRepository assignmentRepository;
    private final SolverConfigurationRepository configurationRepository;
    private final EmployeeRepository employeeRepository;
    private final ConstraintRequestRepository constraintRequestRepository;

    @EventListener
    public SolverConfigurationHasBeenSaved handle(SaveSolverConfigurationCommand command) {
        log.debug("Handling SaveSolverConfigurationCommand");
        var id = command.id();
        if (configurationRepository.existsById(id)) {
            throw new IllegalArgumentException("Solver configuration with id [" + id + "] already exists!");
        }
        var savedConfig = saveConfiguration(
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
        if (!configurationRepository.existsById(command.id())) {
            throw new IllegalArgumentException("Solver configuration with id [" + command.id() + "] does not exists!");
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
        if (!configurationRepository.existsById(command.configurationId())) {
            return null;
        }
        configurationRepository.deleteById(command.configurationId());
        return new SolverConfigurationHasBeenDeleted(command.configurationId());
    }

    private SolverConfigurationRecord saveConfiguration(
            SolverConfigurationId id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            List<EmployeeAssignmentDTO> assignedEmployees,
            List<ConstraintRequestDTO> constraints
    ) {
        var solverConfigurationRecord = new SolverConfigurationRecord(id, name, startDate, endDate);
        var assignedEmployeeIds = assignedEmployees.stream()
                .map(EmployeeAssignmentDTO::getEmployee)
                .toList();
        var assignedEmployeeRecords = StreamSupport.stream(
                employeeRepository.findAllById(assignedEmployeeIds).spliterator(),
                false
        ).toList();
        assignedEmployees.stream()
                .map(assignment -> {
                    var employeeId = new EmployeeId(assignment.getEmployee().getId());
                    var employeeRecord = this.getEmployee(employeeId, assignedEmployeeRecords);
                    var assignmentRecord = new EmployeeAssignmentRecord(
                            solverConfigurationRecord,
                            employeeRecord,
                            assignment.getIndex(),
                            assignment.getWeight()
                    );
                    employeeRecord.getAssignments().add(assignmentRecord);
                    employeeRepository.save(employeeRecord);
                    return assignmentRecord;
                }).forEach(assignmentRepository::save);
        constraints.stream()
                .map(dto -> {
                    var constraint = ConstraintMapper.fromDto(dto);
                    var constraintRecord = new ConstraintRequestRecord(ConstraintId.random(), constraint, solverConfigurationRecord);
                    if (constraint instanceof AbstractEmployeeSpecificConstraint aesc) {
                        var employeeRecord = this.getEmployee(aesc.getOwner(), assignedEmployeeRecords);
                        constraintRecord.setOwner(employeeRecord);
                    }
                    return constraintRecord;
                }).forEach(constraintRequestRepository::save);
        return configurationRepository.save(solverConfigurationRecord);
    }

    private EmployeeRecord getEmployee(EmployeeId employeeId, List<EmployeeRecord> records) {
        return records.stream()
                .filter(e -> e.getId().equals(employeeId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Employee with id [" + employeeId + "] does not exists!"));
    }


}
