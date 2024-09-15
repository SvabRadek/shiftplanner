package com.cocroachden.planner.constraint.repository;

import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity(name = "constraint_record")
@NoArgsConstructor
@Getter
@Accessors(chain = true)
public class ConstraintRecord {
  @Setter
  @Id
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  private EmployeeRecord owner;

  @ManyToOne(fetch = FetchType.LAZY)
  private SolverConfigurationRecord parent;

  private ConstraintType type;

  @Column(length = 1024)
  private SolverConstraint request;

  public ConstraintRecord setRequest(SolverConstraint request) {
    this.type = request.getType();
    this.request = request;
    return this;
  }

  public ConstraintRecord setParent(SolverConfigurationRecord parent) {
    if (this.parent != null) return this;
    parent.addConstraint(this);
    this.parent = parent;
    return this;
  }

  public ConstraintRecord setOwner(EmployeeRecord owner) {
    if (owner == null) return this;
    owner.addConstraint(this);
    this.owner = owner;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ConstraintRecord other) {
      return this.id.equals(other.id);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
