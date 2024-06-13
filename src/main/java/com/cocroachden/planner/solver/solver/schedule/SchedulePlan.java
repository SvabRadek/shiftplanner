package com.cocroachden.planner.solver.solver.schedule;


import com.cocroachden.planner.core.identity.WorkerId;
import com.cocroachden.planner.solver.solver.SolverConfiguration;
import com.google.ortools.sat.CpModel;
import lombok.Getter;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class SchedulePlan {

  private final Map<WorkerId, Map<LocalDate, WorkDay>> assignments;
  private final Map<WorkerId, ScheduleWorker> workers;

  public SchedulePlan(SolverConfiguration configuration, CpModel model) {
    var endDateExclusive = configuration.endDate().plusDays(1);
    var assignments = new HashMap<WorkerId, Map<LocalDate, WorkDay>>();
    configuration.scheduleWorkers().forEach(worker -> {
      var days = new HashMap<LocalDate, WorkDay>();
      configuration.startDate().datesUntil(endDateExclusive).forEach(date -> {
        var workDay = this.createWorkDay(model, worker.workerId(), date);
        days.put(date, workDay);
      });
      assignments.put(worker.workerId(), days);
    });
    var workersAsMap = configuration.scheduleWorkers().stream()
        .collect(Collectors.toMap(ScheduleWorker::workerId, scheduleWorker -> scheduleWorker));
    this.assignments = assignments;
    this.workers = workersAsMap;
  }

  public LocalDate getStartDate() {
    return assignments.values().stream().toList()
        .get(0).values().stream().toList()
        .get(0).date();
  }

  public LocalDate getLastDate() {
    var days = assignments.values().stream().toList().get(0);
    return days.values().stream().toList()
        .get(days.size() - 1).date();
  }

  public WorkDay getSpecificDay(WorkerId workerId, LocalDate date) {
    this.ensureSchedulePlanContainsWorkerId(workerId);
    return assignments.get(workerId).get(date);
  }

  public Integer getWeightForWorker(WorkerId workerId) {
    return this.workers.get(workerId).weight();
  }

  public List<WorkDay> getAllDaysForWorker(WorkerId workerId) {
    this.ensureSchedulePlanContainsWorkerId(workerId);
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

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("SchedulePlan:\n").append("\tAssignments:\n");
    this.assignments.forEach((workerId, workDaysMap) -> {
      builder.append("  Worker: ").append(workerId).append("\n");
      workDaysMap.forEach((date, workDay) -> {
        builder.append("    Date: ").append(date).append("\n");
        builder.append("    Work Day: ").append(workDay).append("\n");
      });
      builder.append("\n");
    });

    builder.append("Workers:\n");
    this.workers.forEach((workerId, worker) -> {
      builder.append("  Worker ID: ").append(workerId).append("\n");
      builder.append("  Worker: ").append(worker).append("\n");
    });

    return builder.toString();
  }

  @SneakyThrows
  private void ensureSchedulePlanContainsWorkerId(WorkerId workerId) {
    if (!this.assignments.containsKey(workerId)) {
      throw SchedulePlanException.becauseWorkerIsNotPresent(workerId);
    }
  }

  public static class SchedulePlanException extends Exception {
    private SchedulePlanException(String message) {
      super(message);
    }

    public static SchedulePlanException becauseWorkerIsNotPresent(WorkerId workerId) {
      return new SchedulePlanException("Schedule plan does not contain workerId [%s]".formatted(workerId.toString()));
    }
  }

  private WorkDay createWorkDay(CpModel model, WorkerId owner, LocalDate day) {
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
