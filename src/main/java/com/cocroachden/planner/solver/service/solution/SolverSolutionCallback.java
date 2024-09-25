package com.cocroachden.planner.solver.service.solution;


import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.SolutionStatus;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solver.service.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpSolverSolutionCallback;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
public class SolverSolutionCallback extends CpSolverSolutionCallback {
    private final Consumer<SolverSolutionDTO> fluxSink;
    @Getter
    private Integer currentSolutionCount = 0;
    @Getter
    private SolverSolution latestResponse;
    private double bestObjectiveValue = Double.MAX_VALUE;
    private final Integer solutionLimit;
    private final SchedulePlan schedulePlan;

    public SolverSolutionCallback(
            Consumer<SolverSolutionDTO> fluxSink,
            SchedulePlan schedulePlan
    ) {
        this.fluxSink = fluxSink;
        this.solutionLimit = 999999999;
        this.schedulePlan = schedulePlan;
    }

    @Override
    public void stopSearch() {
        super.stopSearch();
    }

    @Override
    public void onSolutionCallback() {
        var currentObjective = this.objectiveValue();
        if (currentObjective > bestObjectiveValue) return;
        bestObjectiveValue = currentObjective;
        var response = createResponseSchedule();
        var latestResponse = new SolverSolution(response);
        this.latestResponse = latestResponse;
        this.printStatsHeader(currentObjective);
        var employeeMap = new HashMap<String, Map<LocalDate, WorkShifts>>();
        latestResponse.workdays().forEach((employeeId, responseWorkDays) -> {
            var shiftMap = new HashMap<LocalDate, WorkShifts>();
            responseWorkDays.forEach(solutionWorkDay -> {
                shiftMap.put(solutionWorkDay.date(), solutionWorkDay.assignedShift());
            });
            employeeMap.put(employeeId.getId(), shiftMap);
        });
        fluxSink.accept(
                new SolverSolutionDTO(
                        SolutionStatus.OK,
                        currentObjective,
                        currentSolutionCount,
                        employeeMap
                )
        );
        currentSolutionCount++;
        if (currentSolutionCount >= solutionLimit) {
            stopSearch();
        }
    }


    private HashMap<EmployeeId, List<SolutionWorkDay>> createResponseSchedule() {
        var response = new HashMap<EmployeeId, List<SolutionWorkDay>>();
        schedulePlan.getAssignments()
                .entrySet().stream()
                .sorted(Comparator.comparing(value -> value.getKey().getId()))
                .forEach(entry -> {
                    var employeeId = entry.getKey();
                    var assignments = entry.getValue();
                    var responseDays = new ArrayList<SolutionWorkDay>();
                    assignments.values()
                            .forEach(workDay -> {
                                var off = booleanValue(workDay.offShiftAssignment());
                                var day = booleanValue(workDay.dayShiftAssignment());
                                responseDays.add(
                                        new SolutionWorkDay(
                                                workDay.date(),
                                                off ? WorkShifts.OFF : day ? WorkShifts.DAY : WorkShifts.NIGHT
                                        )
                                );
                            });
                    response.put(employeeId, responseDays);
                });
        return response;
    }

    private void printStatsHeader(double currentObjective) {
        double bestBound = this.bestObjectiveBound();
        double distance = currentObjective + (bestBound > 0 ? bestBound : bestBound * -1);
        log.debug("Solution #{}, cost: {}, optimum deviation: {}", currentSolutionCount, currentObjective, distance);
    }
}
