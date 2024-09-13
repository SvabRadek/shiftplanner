package com.cocroachden.planner.solverconfiguration.repository;

import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity(name = "employee_assignment")
@NoArgsConstructor
@Getter
@Setter
public class EmployeeAssignmentRecord implements Serializable {

  @EmbeddedId
  private EmployeeAssignmentId id;
  private Integer index;
  private Integer weight = 1;
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("employeeId")
  private EmployeeRecord employee;
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("configurationId")
  private SolverConfigurationRecord configuration;

  public EmployeeAssignmentRecord(
          SolverConfigurationRecord configuration,
          EmployeeRecord employee,
          Integer index,
          Integer weight
  ) {
    this.id = new EmployeeAssignmentId(employee.getId(), configuration.getId());
    this.index = index;
    this.weight = weight;
    this.employee = employee;
    this.configuration = configuration;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EmployeeAssignmentRecord other) {
      return this.id.equals(other.id);
    }
    return false;
  }
}
