package com.cocroachden.planner.solver.solver.schedule;


import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.constraints.SolverConstraint;
import com.google.ortools.sat.CpModel;
import lombok.Getter;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class SchedulePlan {

    private final Map<EmployeeId, Map<LocalDate, ScheduleDay>> assignments;
    private final Map<EmployeeId, ScheduleEmployee> employees;
    private final List<SolverConstraint> constraints;

    public SchedulePlan(SolverProblemConfiguration configuration, CpModel model) {
        var endDateExclusive = configuration.endDate().plusDays(1);
        var assignments = new HashMap<EmployeeId, Map<LocalDate, ScheduleDay>>();
        configuration.scheduleEmployees().forEach(employee -> {
            var days = new HashMap<LocalDate, ScheduleDay>();
            configuration.startDate().datesUntil(endDateExclusive).forEach(date -> {
                var workDay = this.createWorkDay(model, employee.employeeId(), date);
                days.put(date, workDay);
            });
            assignments.put(employee.employeeId(), days);
        });
        var employeeMap = configuration.scheduleEmployees().stream()
                .collect(Collectors.toMap(ScheduleEmployee::employeeId, scheduleEmployee -> scheduleEmployee));
        this.assignments = assignments;
        this.employees = employeeMap;
        this.constraints = configuration.solverConstraints();
    }

    public <T extends SolverConstraint> List<T> getAllConstraintsOfType(Class<T> type) {
        return this.constraints.stream()
                .filter(c -> c.getClass().isAssignableFrom(type))
                .map(type::cast)
                .toList();
    }

    public Stream<LocalDate> getAllScheduleDatesAsStream() {
        return this.getStartDate().datesUntil(this.getLastDate().plusDays(1));
    }

    public LocalDate getStartDate() {
        return assignments.values().stream().toList()
                .get(0).values().stream()
                .min(Comparator.comparing(ScheduleDay::date))
                .orElseThrow()
                .date();
    }

    public LocalDate getLastDate() {
        var days = assignments.values().stream().toList().get(0);
        return days.values().stream()
                .max(Comparator.comparing(ScheduleDay::date))
                .orElseThrow()
                .date();
    }

    public ScheduleDay getSpecificDay(EmployeeId employeeId, LocalDate date) {
        this.ensureSchedulePlanContainsEmployeeId(employeeId);
        return assignments.get(employeeId).get(date);
    }

    public Integer getWeightForEmployee(EmployeeId employeeId) {
        return this.employees.get(employeeId).weight();
    }

    public List<ScheduleDay> getAllDaysForEmployee(EmployeeId employeeId) {
        this.ensureSchedulePlanContainsEmployeeId(employeeId);
        return assignments.get(employeeId).values().stream().toList();
    }

    public Map<LocalDate, List<ScheduleDay>> getAllAssignmentsByDate() {
        var workDaysByDate = new HashMap<LocalDate, List<ScheduleDay>>();
        this.assignments.forEach((employeeId, workDays) -> {
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
        this.assignments.forEach((employeeId, workDaysMap) -> {
            builder.append("  Employee: ").append(employeeId).append("\n");
            workDaysMap.forEach((date, workDay) -> {
                builder.append("    Date: ").append(date).append("\n");
                builder.append("    Work Day: ").append(workDay).append("\n");
            });
            builder.append("\n");
        });
        builder.append("Employees:\n");
        this.employees.forEach((employeeId, employee) -> {
            builder.append("  Employee ID: ").append(employeeId).append("\n");
            builder.append("  Employee: ").append(employee).append("\n");
        });

        return builder.toString();
    }

    @SneakyThrows
    private void ensureSchedulePlanContainsEmployeeId(EmployeeId employeeId) {
        if (!this.assignments.containsKey(employeeId)) {
            throw SchedulePlanException.becauseEmployeeIsNotPresent(employeeId);
        }
    }

    public static class SchedulePlanException extends Exception {
        private SchedulePlanException(String message) {
            super(message);
        }

        public static SchedulePlanException becauseEmployeeIsNotPresent(EmployeeId employeeId) {
            return new SchedulePlanException("Schedule plan does not contain employeeId [%s]".formatted(employeeId.toString()));
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
