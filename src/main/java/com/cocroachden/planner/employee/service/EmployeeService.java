package com.cocroachden.planner.employee.service;

import com.cocroachden.planner.employee.command.deleteemployee.DeleteEmployeeCommand;
import com.cocroachden.planner.employee.command.deleteemployee.EmployeeHasBeenDeleted;
import com.cocroachden.planner.employee.command.saveemployee.EmployeeHasBeenSaved;
import com.cocroachden.planner.employee.command.saveemployee.SaveEmployeeCommand;
import com.cocroachden.planner.employee.query.EmployeeQuery;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeQuery employeeQuery;
    private final EmployeeRepository employeeRepository;

    @EventListener
    public EmployeeHasBeenSaved handle(SaveEmployeeCommand command) {
        log.debug("Handling SaveEmployeeCommand");
        if (employeeQuery.existsByName(command.firstName(), command.lastName())) {
            throw new IllegalArgumentException("Employee with this name [%s %s] already exists!".formatted(command.firstName(), command.lastName()));
        }
        var employee = new EmployeeRecord()
                .setId(command.employeeId().getId())
                .setFirstName(command.firstName())
                .setLastName(command.lastName());
        var savedEmployee = employeeRepository.save(employee);
        return new EmployeeHasBeenSaved(savedEmployee);
    }

    @EventListener
    public EmployeeHasBeenDeleted handle(DeleteEmployeeCommand command) {
        log.debug("Handling DeleteEmployeeCommand");
        if (!employeeRepository.existsById(command.employeeId().getId())) {
            return null;
        }
        employeeRepository.deleteById(command.employeeId().getId());
        return new EmployeeHasBeenDeleted(command.employeeId());
    }
}
