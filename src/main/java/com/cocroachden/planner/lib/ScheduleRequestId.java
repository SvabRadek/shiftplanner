package com.cocroachden.planner.lib;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleRequestId extends AbstractIdentity {
  @Serial
  private static final long serialVersionUID = -7692638585201714516L;
  public ScheduleRequestId(String id) {
    super(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ScheduleRequestId other) {
      return other.id.equals(id);
    }
    return false;
  }
}
