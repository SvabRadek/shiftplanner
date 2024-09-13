package com.cocroachden.planner.solverconfiguration;

import com.cocroachden.planner.employee.EmployeeId;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeAssignmentId implements Serializable {
  private EmployeeId employeeId;
  private SolverConfigurationId configurationId;
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EmployeeAssignmentId other) {
      return employeeId.equals(other.getEmployeeId()) && configurationId.equals(other.getConfigurationId());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(employeeId.getId() + configurationId.getId());
  }
}
