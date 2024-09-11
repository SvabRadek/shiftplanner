package com.cocroachden.planner.solver.repository;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class EmployeeAssignmentId implements Serializable {
  private String employeeId;
  private String configurationId;

  @Override
  public int hashCode() {
    return Objects.hash(employeeId, configurationId);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EmployeeAssignmentId other) {
      return other.employeeId.equals(this.employeeId) && other.configurationId.equals(this.configurationId);
    }
    return false;
  }
}
