package com.cocroachden.planner.solver.schedule.request;

import com.cockroachden.shiftschedulerback.lib.ScheduleRequestId;

public interface ScheduleRequest {
  ScheduleRequestId getId();
}
