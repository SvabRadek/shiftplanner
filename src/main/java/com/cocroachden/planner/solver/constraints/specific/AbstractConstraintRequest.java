package com.cocroachden.planner.solver.constraints.specific;


import com.cocroachden.planner.lib.ConstraintType;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("AbstractConstraintRequest")
public abstract class AbstractConstraintRequest implements ConstraintRequest {
  private ConstraintType type;
  private WorkerId owner;

  @Override
  public ConstraintType getType() {
    return this.type;
  }

  public Optional<WorkerId> getOwner() {
    return Optional.ofNullable(this.owner);
  }
}
