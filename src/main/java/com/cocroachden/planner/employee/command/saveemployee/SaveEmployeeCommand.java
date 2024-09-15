package com.cocroachden.planner.employee.command.saveemployee;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.employee.EmployeeId;

public record SaveEmployeeCommand(
        EmployeeId employeeId,
        String firstName,
        String lastName
) implements Command {
}
