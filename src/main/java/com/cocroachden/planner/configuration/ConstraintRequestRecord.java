package com.cocroachden.planner.configuration;

import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "constraint_request")
@Table(name = "constraint_requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConstraintRequestRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Convert(converter = ConstraintRequestAttributeConverter.class)
  @Column(length = 1024)
  private ConstraintRequest request;
}
