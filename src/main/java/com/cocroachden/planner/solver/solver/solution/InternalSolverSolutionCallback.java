package com.cocroachden.planner.solver.solver.solution;


import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solver.SolutionStatus;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.WorkShifts;
import com.cocroachden.planner.solver.solver.schedule.SchedulePlan;
import com.google.ortools.sat.CpSolverSolutionCallback;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class InternalSolverSolutionCallback extends CpSolverSolutionCallback {
    private final Consumer<SolverSolutionDTO> fluxSink;
    @Getter
    private Integer currentSolutionCount = 0;
    @Getter
    private SolverSolution latestSolution;
    private double bestObjectiveValue = Double.MAX_VALUE;
    private final SchedulePlan schedulePlan;

    public InternalSolverSolutionCallback(
            Consumer<SolverSolutionDTO> fluxSink,
            SchedulePlan schedulePlan
    ) {
        this.fluxSink = fluxSink;
        this.schedulePlan = schedulePlan;
    }

    @Override
    public void onSolutionCallback() {
        var currentObjective = this.objectiveValue();
        if (currentObjective > bestObjectiveValue) return;
        bestObjectiveValue = currentObjective;
        this.latestSolution = new SolverSolution(this.readAssignments());
        this.printDebug(currentObjective);
        var employeeMap = new HashMap<String, Map<LocalDate, WorkShifts>>();
        latestSolution.workdays().forEach((employeeId, solutionWorkDays) -> {
            var shiftMap = solutionWorkDays.stream()
                    .collect(Collectors.toMap(
                            SolutionWorkDay::date,
                            SolutionWorkDay::assignedShift
                    ));
            employeeMap.put(employeeId.getId(), shiftMap);
        });
        fluxSink.accept(
                new SolverSolutionDTO(
                        SolutionStatus.OK,
                        currentObjective,
                        currentSolutionCount,
                        "Feasible solution found!",
                        employeeMap
                )
        );
        currentSolutionCount++;
    }


    private HashMap<EmployeeId, List<SolutionWorkDay>> readAssignments() {
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

    private void printDebug(double currentObjective) {
        double bestBound = this.bestObjectiveBound();
        double distance = currentObjective + (bestBound > 0 ? bestBound : bestBound * -1);
        log.debug("Solution #{}, cost: {}, optimum deviation: {}", currentSolutionCount, currentObjective, distance);
    }
}
