package com.cocroachden.planner.lib;

public class ScheduleRequestId extends AbstractIdentity {
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
