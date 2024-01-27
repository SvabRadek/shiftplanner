package com.cocroachden.planner.constraint.repository;

import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "constraint_requests")
@NoArgsConstructor
@Getter
public class ConstraintRequestRecord {
  @Id
  @GeneratedValue
  @Setter
  private UUID id;
  private UUID ownerConfiguration;
  @NotEmpty
  private String type;
  @Convert(converter = ConstraintRequestAttributeConverter.class)
  @Column(length = 1024)
  private ConstraintRequest request;

  public ConstraintRequestRecord(
      UUID ownerConfiguration, ConstraintRequest request
  ) {
    this.request = request;
    this.type = request.getId().getId();
    this.ownerConfiguration = ownerConfiguration;
  }
}
