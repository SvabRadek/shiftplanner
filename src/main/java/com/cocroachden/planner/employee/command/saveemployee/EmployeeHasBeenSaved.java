package com.cocroachden.planner.employee.command.saveemployee;

import com.cocroachden.planner.common.messaging.Event;
import com.cocroachden.planner.employee.repository.EmployeeRecord;

public record EmployeeHasBeenSaved(EmployeeRecord employeeRecord) implements Event {
}
