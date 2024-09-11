package com.cocroachden.planner.employee.command.deleteemployee;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.common.messaging.Event;

public record EmployeeHasBeenDeleted(
        EmployeeId employeeId
) implements Event {
}
