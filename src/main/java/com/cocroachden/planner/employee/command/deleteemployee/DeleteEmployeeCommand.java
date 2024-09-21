package com.cocroachden.planner.employee.command.deleteemployee;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.common.messaging.Command;

public record DeleteEmployeeCommand(
        EmployeeId employeeId,
        String currentUser
) implements Command {
}
