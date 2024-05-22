package com.cocroachden.planner.solver;


import com.cocroachden.planner.solver.schedule.ScheduleWorker;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

public record SchedulePlanConfiguration(
    LocalDate startDate,
    LocalDate endDate,
    List<ScheduleWorker> scheduleWorkers
) {
  @Override
  public String toString() {
    return "{ startDate: %s, endDate: %s, scheduleWorkers: [%s] }".formatted(
        startDate.toString(),
        endDate.toString(),
        StringUtils.join(scheduleWorkers.stream().map(w -> w.workerId().toString()).toList(), ", ")
    );
  }
}
