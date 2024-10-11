package com.cocroachden.planner.solverconfiguration;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class EmployeeAssignmentId implements Serializable {
  private String employeeId;
  private String configurationId;
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EmployeeAssignmentId other) {
      return employeeId.equals(other.getEmployeeId()) && configurationId.equals(other.getConfigurationId());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(employeeId + configurationId);
  }

  @Override
  public String toString() {
    return employeeId + configurationId;
  }
}
