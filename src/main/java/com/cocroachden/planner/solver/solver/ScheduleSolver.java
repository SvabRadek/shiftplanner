package com.cocroachden.planner.solver.solver;


import com.cocroachden.planner.solver.SchedulePlanConfiguration;
import com.cocroachden.planner.solver.ScheduleResultDTO;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.GenericConstraintApplier;
import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlanBuilder;
import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public class ScheduleSolver {
  private final GenericConstraintApplier constraintApplier;
  private ScheduleSolutionCb solutionCb;
  private Thread solverThread;
  private CpSolver cpSolver;

  public void solve(
      Consumer<ScheduleResultDTO> fluxSink,
      SchedulePlanConfiguration schedulePlanConfiguration,
      List<ConstraintRequest> constraintRequests
  ) {
    if (this.solverThread != null && this.solverThread.isAlive()) {
      this.solverThread.interrupt();
    }
    if (this.solutionCb != null) {
      this.solutionCb.stopSearch();
    }
    Loader.loadNativeLibraries();
    var model = new CpModel();
    var objectives = new Objectives();
    var schedulePlan = SchedulePlanBuilder.create(schedulePlanConfiguration, model);
    constraintRequests.forEach(request -> constraintApplier.apply(schedulePlan, model, objectives, request));
    this.cpSolver = new CpSolver();
    this.cpSolver.getParameters().setLinearizationLevel(0);
    this.cpSolver.getParameters().setEnumerateAllSolutions(true);
    this.cpSolver.getParameters().setRelativeGapLimit(0.05);
    model.minimize(objectives.getObjectiveAsExpression());
    this.solutionCb = new ScheduleSolutionCb(
        fluxSink,
        schedulePlan
    );
    this.solverThread = new Thread(() -> {
      var status = this.cpSolver.solve(model, this.solutionCb);
      log.debug("Status: {}", status);
      log.debug("{} solutions found.", this.solutionCb.getCurrentSolutionCount());
      log.debug("Statistics");
      log.debug("  conflicts: {}", this.cpSolver.numConflicts());
      log.debug("  branches : {}", this.cpSolver.numBranches());
      log.debug("  wall time: {}", this.cpSolver.wallTime());
    });
    this.solverThread.setName("solver-thread");
    log.info("Starting solver thread...");
    this.solverThread.start();
  }

  public void stop() {
    if (this.cpSolver != null) {
      this.cpSolver.stopSearch();
      this.solverThread.interrupt();
      log.info("Solver stopped.");
    }
  }
}
