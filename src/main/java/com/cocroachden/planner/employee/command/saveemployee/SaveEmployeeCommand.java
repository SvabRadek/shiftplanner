package com.cocroachden.planner.employee.command.saveemployee;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import dev.hilla.Nonnull;

public record SaveEmployeeCommand(
        EmployeeId id,
        String firstName,
        String lastName
) implements Command {
}
