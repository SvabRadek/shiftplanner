package com.cocroachden.planner.lib;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkerId extends AbstractIdentity {
  public WorkerId(String id) {
    super(id);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof WorkerId workerId) {
      return this.id.equals(workerId.id);
    }
    return false;
  }
}
