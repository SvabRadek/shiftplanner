package com.cocroachden.planner.solver.solver;


import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.solver.schedule.SchedulePlan;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import com.cocroachden.planner.solver.solver.response.ResponseSchedule;
import com.cocroachden.planner.solver.solver.response.ResponseWorkDay;
import com.google.ortools.sat.CpSolverSolutionCallback;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class ScheduleSolutionCb extends CpSolverSolutionCallback {
  @Getter
  private Integer currentSolutionCount = 0;
  @Getter
  private ResponseSchedule latestResponse;
  private double bestObjectiveValue = Double.MAX_VALUE;
  private final Integer solutionLimit;
  private final SchedulePlan schedulePlan;


  public ScheduleSolutionCb(SchedulePlan schedulePlan) {
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
    var latestResponse = new ResponseSchedule(response);
    this.printStatsHeader(currentObjective);
    this.latestResponse = latestResponse;
    currentSolutionCount++;
    if (currentSolutionCount >= solutionLimit) {
      stopSearch();
    }
  }

  private HashMap<WorkerId, List<ResponseWorkDay>> createResponseSchedule() {
    var response = new HashMap<WorkerId, List<ResponseWorkDay>>();
    schedulePlan.assignments()
        .entrySet().stream()
        .sorted(Comparator.comparingInt(value -> Integer.parseInt(value.getKey().getWorkerId())))
        .forEach(entry -> {
          var workerId = entry.getKey();
          var assignments = entry.getValue();
          var responseDays = new ArrayList<ResponseWorkDay>();
          assignments.values()
              .forEach(workDay -> {
                var off = booleanValue(workDay.offShiftAssignment());
                var day = booleanValue(workDay.dayShiftAssignment());
                responseDays.add(new ResponseWorkDay(
                    workDay.date(),
                    off ? WorkShifts.OFF : day ? WorkShifts.DAY : WorkShifts.NIGHT
                ));
              });
          response.put(workerId, responseDays);
        });
    return response;
  }

  private void printStatsHeader(double currentObjective) {
    double bestBound = this.bestObjectiveBound();
    double distance = currentObjective + (bestBound > 0 ? bestBound : bestBound * -1);
    log.debug("Solution #{}, cost: {}, optimum deviation: {}", currentSolutionCount, currentObjective, distance);
  }
}
