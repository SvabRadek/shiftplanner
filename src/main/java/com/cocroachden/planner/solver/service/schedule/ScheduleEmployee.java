package com.cocroachden.planner.solver.service.schedule;


import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;

public record ScheduleEmployee(
    EmployeeId employeeId,
    Integer weight
) {

  public static ScheduleEmployee from(EmployeeAssignmentRecord assignment) {
    return new ScheduleEmployee(
        assignment.getEmployee().getId(),
        assignment.getWeight()
    );
  }

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
