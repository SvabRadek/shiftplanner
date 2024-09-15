package com.cocroachden.planner.solverconfiguration.repository;

import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity(name = "employee_assignment")
@NoArgsConstructor
@Getter
@Accessors(chain = true)
public class EmployeeAssignmentRecord implements Serializable {

  @EmbeddedId
  private EmployeeAssignmentId id = new EmployeeAssignmentId();
  @Setter
  private Integer index;
  @Setter
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
    if (obj instanceof EmployeeAssignmentRecord other) {
      return this.id.equals(other.id);
    }
    return false;
  }

  public EmployeeAssignmentRecord setEmployee(EmployeeRecord employee) {
    if(employee == null) return this;
    employee.addAssignment(this);
    this.employee = employee;
    return this;
  }

  public EmployeeAssignmentRecord setConfiguration(SolverConfigurationRecord configuration) {
    if(configuration == null) return this;
    configuration.addAssignment(this);
    this.configuration = configuration;
    return this;
  }
}
