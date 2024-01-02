package com.cocroachden.planner.solver;


import com.cocroachden.planner.solver.schedule.ScheduleWorker;

import java.time.LocalDate;
import java.util.List;

public record SchedulePlanConfiguration(
    LocalDate startDate,
    LocalDate endDate,
    List<ScheduleWorker> scheduleWorkers
) {

}
