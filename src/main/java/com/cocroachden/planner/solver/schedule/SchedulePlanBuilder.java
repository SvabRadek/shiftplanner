package com.cocroachden.planner.solver.schedule;

import com.cockroachden.shiftschedulerback.lib.WorkerId;
import com.cockroachden.shiftschedulerback.model.SchedulePlanConfiguration;
import com.google.ortools.sat.CpModel;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SchedulePlanBuilder {
  private final Map<WorkerId, Map<LocalDate, WorkDay>> assignment;

  public SchedulePlanBuilder() {
    this.assignment = new HashMap<>();
  }

  public static SchedulePlan create(
      SchedulePlanConfiguration configuration,
      CpModel model
  ) {
    var endDateExclusive = configuration.endDate().plusDays(1);
    var assignments = new HashMap<WorkerId, Map<LocalDate, WorkDay>>();
    configuration.scheduleWorkers().forEach(worker -> {
      var days = new HashMap<LocalDate, WorkDay>();
      configuration.startDate().datesUntil(endDateExclusive).forEach(date -> {
        var workDay = SchedulePlanBuilder.createWorkDay(model, worker.workerId(), date);
        days.put(date, workDay);
      });
      assignments.put(worker.workerId(), days);
    });
    var workersAsMap = configuration.scheduleWorkers().stream()
        .collect(Collectors.toMap(ScheduleWorker::workerId, scheduleWorker -> scheduleWorker));
    return new SchedulePlan(assignments, workersAsMap);
  }

  public SchedulePlanBuilder addWorker(ScheduleWorker worker) {
    assignment.put(worker.workerId(), new HashMap<>());
    return this;
  }

  public SchedulePlanBuilder addDay(ScheduleWorker worker) {
    assignment.put(worker.workerId(), new HashMap<>());
    return this;
  }

  private static WorkDay createWorkDay(CpModel model, WorkerId owner, LocalDate day) {
    var dayVarName = "W%sD%sS".formatted(owner, day.toEpochDay());
    return new WorkDay(
        owner,
        day,
        model.newBoolVar(dayVarName + 0),
        model.newBoolVar(dayVarName + 1),
        model.newBoolVar(dayVarName + 2)
    );
  }

}
