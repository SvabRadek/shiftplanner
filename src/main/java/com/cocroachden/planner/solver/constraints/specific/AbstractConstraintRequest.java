package com.cocroachden.planner.solver.constraints.specific;


import com.cocroachden.planner.lib.ConstraintRequestId;
import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AbstractConstraintRequest implements ConstraintRequest {
  private ConstraintRequestId id;
  private WorkerId owner;
  @Override
  public ConstraintRequestId getId() {
    return this.id;
  }

  public Optional<WorkerId> getOwner() {
    return Optional.ofNullable(this.owner);
  }
}
