package com.cocroachden.planner.solver.repository;

import com.cocroachden.planner.employee.EmployeeRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity(name = "EmployeeAssignment")
@Table
@NoArgsConstructor
@Getter
@Setter
public class EmployeeAssignment implements Serializable {
  @EmbeddedId
  private EmployeeAssignmentId id = new EmployeeAssignmentId();
  private Integer index;
  private Integer weight = 1;
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("employeeId")
  private EmployeeRecord employee;
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("configurationId")
  private SolverConfigurationRecord configuration;

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EmployeeAssignment other) {
      return this.id.equals(other.id);
    }
    return false;
  }
}
