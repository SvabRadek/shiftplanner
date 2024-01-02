package com.cocroachden.planner.solver.solver;

import com.cockroachden.shiftschedulerback.model.SchedulePlanConfiguration;
import com.cockroachden.shiftschedulerback.model.constraints.ConstraintRequest;
import com.cockroachden.shiftschedulerback.model.constraints.GenericConstraintApplier;
import com.cockroachden.shiftschedulerback.model.schedule.Objectives;
import com.cockroachden.shiftschedulerback.model.schedule.SchedulePlanBuilder;
import com.cockroachden.shiftschedulerback.model.schedule.request.ScheduleRequest;
import com.cockroachden.shiftschedulerback.model.solver.response.ResponseSchedule;
import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
public class ScheduleSolver {
  private final GenericConstraintApplier constraintApplier;
  private ScheduleSolutionCb solutionCb;

  @Async
  public CompletableFuture<ResponseSchedule> solve(
      SchedulePlanConfiguration schedulePlanConfiguration,
      List<ScheduleRequest> scheduleRequests,
      List<ConstraintRequest> constraintRequests,
      int timeLimitInSeconds
  ) {
    Loader.loadNativeLibraries();
    var model = new CpModel();
    var objectives = new Objectives();
    var schedulePlan = SchedulePlanBuilder.create(schedulePlanConfiguration, model);
    constraintRequests.forEach(request -> constraintApplier.apply(schedulePlan, model, objectives, request));
    var solver = new CpSolver();
    solver.getParameters().setLinearizationLevel(0);
    solver.getParameters().setEnumerateAllSolutions(true);
    solver.getParameters().setRelativeGapLimit(0.05);
    solver.getParameters().setMaxTimeInSeconds(timeLimitInSeconds);
    model.minimize(objectives.getObjectiveAsExpression());
    this.solutionCb = new ScheduleSolutionCb(schedulePlan);
    log.debug("Initiating solution search...");
    var status = solver.solve(model, this.solutionCb);
    log.debug("Status: {}", status);
    log.debug("{} solutions found.", this.solutionCb.getCurrentSolutionCount());
    log.debug("Statistics");
    log.debug("  conflicts: {}", solver.numConflicts());
    log.debug("  branches : {}", solver.numBranches());
    log.debug("  wall time: {}", solver.wallTime());
    return CompletableFuture.completedFuture(this.solutionCb.getLatestResponse());
  }

  public void stop() {
    if (this.solutionCb != null) {
      this.solutionCb.stopSearch();
    }
    log.debug("Stopped.");
  }
}
