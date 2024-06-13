package com.cocroachden.planner.solver.solver.schedule;


import com.cocroachden.planner.core.identity.WorkerId;

public record ScheduleWorker(
    WorkerId workerId,
    Integer weight
) {
  @Override
  public int hashCode() {
    return workerId.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ScheduleWorker other) {
      return other.workerId.equals(workerId);
    }
    return false;
  }

  @Override
  public String toString() {
    return this.workerId.getId().toString();
  }
}
