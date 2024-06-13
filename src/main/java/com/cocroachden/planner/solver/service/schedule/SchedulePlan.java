package com.cocroachden.planner.solver.service.schedule;


import com.cocroachden.planner.employee.api.EmployeeId;
import com.cocroachden.planner.solver.service.SolverConfiguration;
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

  private final Map<EmployeeId, Map<LocalDate, ScheduleDay>> assignments;
  private final Map<EmployeeId, ScheduleEmployee> workers;

  public SchedulePlan(SolverConfiguration configuration, CpModel model) {
    var endDateExclusive = configuration.endDate().plusDays(1);
    var assignments = new HashMap<EmployeeId, Map<LocalDate, ScheduleDay>>();
    configuration.scheduleEmployees().forEach(worker -> {
      var days = new HashMap<LocalDate, ScheduleDay>();
      configuration.startDate().datesUntil(endDateExclusive).forEach(date -> {
        var workDay = this.createWorkDay(model, worker.employeeId(), date);
        days.put(date, workDay);
      });
      assignments.put(worker.employeeId(), days);
    });
    var workersAsMap = configuration.scheduleEmployees().stream()
        .collect(Collectors.toMap(ScheduleEmployee::employeeId, scheduleEmployee -> scheduleEmployee));
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

  public ScheduleDay getSpecificDay(EmployeeId employeeId, LocalDate date) {
    this.ensureSchedulePlanContainsWorkerId(employeeId);
    return assignments.get(employeeId).get(date);
  }

  public Integer getWeightForWorker(EmployeeId employeeId) {
    return this.workers.get(employeeId).weight();
  }

  public List<ScheduleDay> getAllDaysForWorker(EmployeeId employeeId) {
    this.ensureSchedulePlanContainsWorkerId(employeeId);
    return assignments.get(employeeId).values().stream().toList();
  }

  public Map<LocalDate, List<ScheduleDay>> getAllAssignmentsByDate() {
    var workDaysByDate = new HashMap<LocalDate, List<ScheduleDay>>();
    this.assignments.forEach((workerId, workDays) -> {
      workDays.forEach((date, workDay) -> {
        if (workDaysByDate.containsKey(date)) {
          workDaysByDate.get(date).add(workDay);
        } else {
          var list = new ArrayList<ScheduleDay>();
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
  private void ensureSchedulePlanContainsWorkerId(EmployeeId employeeId) {
    if (!this.assignments.containsKey(employeeId)) {
      throw SchedulePlanException.becauseWorkerIsNotPresent(employeeId);
    }
  }

  public static class SchedulePlanException extends Exception {
    private SchedulePlanException(String message) {
      super(message);
    }

    public static SchedulePlanException becauseWorkerIsNotPresent(EmployeeId employeeId) {
      return new SchedulePlanException("Schedule plan does not contain workerId [%s]".formatted(employeeId.toString()));
    }
  }

  private ScheduleDay createWorkDay(CpModel model, EmployeeId owner, LocalDate day) {
    var dayVarName = "W%sD%sS".formatted(owner, day.toEpochDay());
    return new ScheduleDay(
        owner,
        day,
        model.newBoolVar(dayVarName + 0),
        model.newBoolVar(dayVarName + 1),
        model.newBoolVar(dayVarName + 2)
    );
  }
}
