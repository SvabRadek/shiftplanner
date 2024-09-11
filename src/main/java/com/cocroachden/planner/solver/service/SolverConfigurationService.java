package com.cocroachden.planner.solver.service;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.SolverConfigurationId;
import com.cocroachden.planner.solver.command.deleteconfiguration.DeleteSolverConfigurationCommand;
import com.cocroachden.planner.solver.command.deleteconfiguration.SolverConfigurationHasBeenDeleted;
import com.cocroachden.planner.solver.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solver.command.saveconfiguration.SolverConfigurationHasBeenSaved;
import com.cocroachden.planner.solver.repository.EmployeeAssignment;
import com.cocroachden.planner.solver.repository.EmployeeAssignmentRepository;
import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SolverConfigurationService {

    private final EmployeeAssignmentRepository assignmentRepository;
    private final SolverConfigurationRepository configurationRepository;
    private final EmployeeRepository employeeRepository;

    @EventListener
    public SolverConfigurationHasBeenSaved handle(SaveSolverConfigurationCommand command) {
        var id = new SolverConfigurationId(command.solverConfigurationDTO().getId());
        if (configurationRepository.existsById(id)) {
            throw new IllegalArgumentException("Solver configuration with id [" + id + "] already exists!");
        }
        var record = new SolverConfigurationRecord(
                id,
                command.solverConfigurationDTO().getName(),
                command.solverConfigurationDTO().getStartDate(),
                command.solverConfigurationDTO().getEndDate()
        );
        command.solverConfigurationDTO().getEmployees().stream()
                .map(a -> {
                    var assignment = new EmployeeAssignment();
                    assignment.setConfiguration(record);
                    assignment.setWeight(a.getWeight());
                    assignment.setIndex(a.getIndex());
                    assignment.setEmployee(employeeRepository.findById(new EmployeeId(a.getEmployee().getId()))
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Employee [%s] assigned to configuration [%s] does not exist!".formatted(
                                            a.getEmployee().getId(),
                                            id.getId())
                            )));
                    return assignment;
                }).forEach(assignmentRepository::save);
        var savedConfig = configurationRepository.save(record);
        return new SolverConfigurationHasBeenSaved(savedConfig);
    }

    @EventListener
    public SolverConfigurationHasBeenDeleted handle(DeleteSolverConfigurationCommand command) {
        configurationRepository.deleteById(command.configurationId());
        return new SolverConfigurationHasBeenDeleted(command.configurationId());
    }

}
