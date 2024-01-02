package com.cocroachden.planner.solver.schedule;

import com.cockroachden.shiftschedulerback.model.schedule.request.ScheduleRequest;

public interface ScheduleRequestResolver {

  boolean supports(ScheduleRequest request);
}
