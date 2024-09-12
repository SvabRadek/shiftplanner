package com.cocroachden.planner.solver.repository;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeAssignmentId implements Serializable {
  private String id;
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EmployeeAssignmentId other) {
      return id.equals(other.id);
    }
    return false;
  }
}
