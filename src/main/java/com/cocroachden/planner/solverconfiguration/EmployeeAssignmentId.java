package com.cocroachden.planner.solverconfiguration;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Accessors(chain = true)
public class EmployeeAssignmentId implements Serializable {
  @Column(name = "employee_id")
  private String employeeId;
  @Column(name = "configuration_id")
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
    return Objects.hash(employeeId, configurationId);
  }

  @Override
  public String toString() {
    return employeeId + configurationId;
  }
}
