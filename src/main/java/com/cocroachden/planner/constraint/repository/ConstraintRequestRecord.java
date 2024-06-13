package com.cocroachden.planner.constraint.repository;

import com.cocroachden.planner.constraint.api.ConstraintType;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "ConstraintRequest")
@Table(name = "constraint_request")
@NoArgsConstructor
@Getter
@Setter
public class ConstraintRequestRecord {
  @Id
  @GeneratedValue
  private UUID id;
  @ManyToOne(fetch = FetchType.LAZY)
  private SolverConfigurationRecord parent;
  private ConstraintType type;
  @Column(length = 1024)
  private ConstraintRequest request;

  public ConstraintRequestRecord(ConstraintRequest request) {
    this.request = request;
    this.type = request.getType();
  }
}
