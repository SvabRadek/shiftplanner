package com.cocroachden.planner.lib;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkerId extends AbstractIdentity {
  @Serial
  private static final long serialVersionUID = -3136703610267018121L;
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
