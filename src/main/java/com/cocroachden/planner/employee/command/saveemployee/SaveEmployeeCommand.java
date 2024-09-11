package com.cocroachden.planner.employee.command.saveemployee;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.employee.EmployeeDTO;

public record SaveEmployeeCommand(EmployeeDTO employeeDTO) implements Command {
}
