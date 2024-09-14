package com.cocroachden.planner.constraint.repository;

import com.cocroachden.planner.constraint.ConstraintId;
import com.cocroachden.planner.constraint.ConstraintType;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ConstraintRequest")
@Table(name = "constraint_request")
@NoArgsConstructor
@Getter
@Setter
public class ConstraintRequestRecord {
  @EmbeddedId
  @AttributeOverride(name = "id", column = @Column(name = "constraint_request_id"))
  private ConstraintId id;

  @ManyToOne(fetch = FetchType.LAZY)
  private EmployeeRecord owner;

  @ManyToOne(fetch = FetchType.LAZY)
  private SolverConfigurationRecord parent;

  private ConstraintType type;

  @Column(length = 1024)
  private ConstraintRequest request;

  public ConstraintRequestRecord(
          ConstraintId constraintId,
          ConstraintRequest request,
          SolverConfigurationRecord parent) {
    this.id = constraintId;
    this.request = request;
    this.type = request.getType();
    this.parent = parent;
  }

  public ConstraintRequestRecord(
          ConstraintId constraintId,
          ConstraintRequest request,
          SolverConfigurationRecord parent,
          EmployeeRecord owner
  ) {
    this.id = constraintId;
    this.owner = owner;
    this.request = request;
    this.type = request.getType();
    this.parent = parent;
  }
}
