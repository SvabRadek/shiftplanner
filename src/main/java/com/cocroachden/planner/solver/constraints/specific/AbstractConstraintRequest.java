package com.cocroachden.planner.solver.constraints.specific;


import com.cocroachden.planner.lib.ConstraintRequestTypeId;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("AbstractConstraintRequest")
public abstract class AbstractConstraintRequest implements ConstraintRequest {
  private ConstraintRequestTypeId id;
  private WorkerId owner;
  @Override
  public ConstraintRequestTypeId getId() {
    return this.id;
  }

  public Optional<WorkerId> getOwner() {
    return Optional.ofNullable(this.owner);
  }
}
