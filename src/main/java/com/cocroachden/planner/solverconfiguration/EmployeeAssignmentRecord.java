package com.cocroachden.planner.solverconfiguration;

import com.cocroachden.planner.employee.EmployeeRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity(name = "employee_assignment")
@Table
@NoArgsConstructor
@Getter
@Setter
public class EmployeeAssignmentRecord implements Serializable {

  @EmbeddedId
  @AttributeOverride(name = "id", column = @Column(name = "employee_assignment_id"))
  private EmployeeAssignmentId id;
  private Integer index;
  private Integer weight = 1;
  @ManyToOne(fetch = FetchType.LAZY)
  private EmployeeRecord employee;
  @ManyToOne(fetch = FetchType.LAZY)
  private SolverConfigurationRecord configuration;

  public EmployeeAssignmentRecord(
          SolverConfigurationRecord configuration,
          EmployeeRecord employee,
          Integer index,
          Integer weight
  ) {
    this.id = new EmployeeAssignmentId(configuration.getId().getId() + employee.getId().getId());
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
