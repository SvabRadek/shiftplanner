package com.cocroachden.planner.employee.service;

import com.cocroachden.planner.employee.command.deleteemployee.DeleteEmployeeCommand;
import com.cocroachden.planner.employee.command.deleteemployee.EmployeeHasBeenDeleted;
import com.cocroachden.planner.employee.command.saveemployee.EmployeeHasBeenSaved;
import com.cocroachden.planner.employee.command.saveemployee.SaveEmployeeCommand;
import com.cocroachden.planner.employee.query.EmployeeQuery;
import com.cocroachden.planner.employee.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeQuery employeeQuery;
    private final EmployeeRepository employeeRepository;

    @EventListener
    public EmployeeHasBeenSaved handle(SaveEmployeeCommand command) {
        var dto = command.employeeDTO();
        if (employeeQuery.existsByName(dto.getFirstName(), dto.getLastName())) {
            throw new IllegalArgumentException("Employee with this name [%s %s] already exists!".formatted(dto.getFirstName(), dto.getLastName()));
        }
        var employee = new EmployeeRecord(
                command.employeeDTO().getFirstName(),
                command.employeeDTO().getLastName()
        );
        var savedEmployee = employeeRepository.save(employee);
        return new EmployeeHasBeenSaved(savedEmployee);
    }

    @EventListener
    public EmployeeHasBeenDeleted handle(DeleteEmployeeCommand command) {
        employeeRepository.deleteById(command.employeeId());
        return new EmployeeHasBeenDeleted(command.employeeId());
    }
}
