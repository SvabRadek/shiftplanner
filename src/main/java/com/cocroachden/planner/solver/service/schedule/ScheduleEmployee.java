package com.cocroachden.planner.solver.service.schedule;


import com.cocroachden.planner.employee.api.EmployeeId;

public record ScheduleEmployee(
    EmployeeId employeeId,
    Integer weight
) {
  @Override
  public int hashCode() {
    return employeeId.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ScheduleEmployee other) {
      return other.employeeId.equals(employeeId);
    }
    return false;
  }

  @Override
  public String toString() {
    return this.employeeId.getId().toString();
  }
}
