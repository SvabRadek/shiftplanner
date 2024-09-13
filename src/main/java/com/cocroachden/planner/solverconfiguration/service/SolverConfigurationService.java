package com.cocroachden.planner.solverconfiguration.service;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.DeleteSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.deleteconfiguration.SolverConfigurationHasBeenDeleted;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SolverConfigurationHasBeenSaved;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentRecord;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRepository;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationRecord;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SolverConfigurationService {
    private final EmployeeAssignmentRepository assignmentRepository;
    private final SolverConfigurationRepository configurationRepository;
    private final EmployeeRepository employeeRepository;

    @EventListener
    public SolverConfigurationHasBeenSaved handle(SaveSolverConfigurationCommand command) {
        log.debug("Handling SaveSolverConfigurationCommand");
        var id = command.id();
        if (configurationRepository.existsById(id)) {
            throw new IllegalArgumentException("Solver configuration with id [" + id + "] already exists!");
        }
        var solverConfigurationRecord = new SolverConfigurationRecord(
                id,
                command.name(),
                command.startDate(),
                command.endDate()
        );
        command.assignedEmployees().stream()
                .map(assignment -> {
                    var employeeId = new EmployeeId(assignment.getEmployee().getId());
                    var employeeRecord = employeeRepository.findById(employeeId)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Employee [%s] assigned to configuration [%s] does not exist!".formatted(
                                            employeeId,
                                            id.getId())
                            ));
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
        var savedConfig = configurationRepository.save(solverConfigurationRecord);
        return new SolverConfigurationHasBeenSaved(savedConfig);
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

}
