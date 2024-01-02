package com.cocroachden.planner.solver.schedule;


import com.cocroachden.planner.lib.WorkerId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SchedulePlan(
    Map<WorkerId, Map<LocalDate, WorkDay>> assignments,
    Map<WorkerId, ScheduleWorker> workers
) {
  public WorkDay getSpecificDay(WorkerId workerId, LocalDate date) {
    return assignments.get(workerId).get(date);
  }

  public Integer getWeightForWorker(WorkerId workerId) {
    return this.workers.get(workerId).weight();
  }

  public List<WorkDay> getAllDaysForWorker(WorkerId workerId) {
    return assignments.get(workerId).values().stream().toList();
  }

  public Map<LocalDate, List<WorkDay>> getAllAssignmentsByDate() {
    var workDaysByDate = new HashMap<LocalDate, List<WorkDay>>();
    this.assignments.forEach((workerId, workDays) -> {
      workDays.forEach((date, workDay) -> {
        if (workDaysByDate.containsKey(date)) {
          workDaysByDate.get(date).add(workDay);
        } else {
          var list = new ArrayList<WorkDay>();
          list.add(workDay);
          workDaysByDate.put(date, list);
        }
      });
    });
    return workDaysByDate;
  }
}
