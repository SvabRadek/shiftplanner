package com.cocroachden.planner.solver.schedule;


import com.cocroachden.planner.solver.schedule.request.ScheduleRequest;

public interface ScheduleRequestResolver {

  boolean supports(ScheduleRequest request);
}
