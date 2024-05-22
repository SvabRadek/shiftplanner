package com.cocroachden.planner.solver.solver;


import com.cocroachden.planner.solver.SchedulePlanConfiguration;
import com.cocroachden.planner.solver.ScheduleResultDTO;
import com.cocroachden.planner.solver.SolutionStatus;
import com.cocroachden.planner.solver.constraints.ConstraintRequest;
import com.cocroachden.planner.solver.constraints.GenericConstraintApplier;
import com.cocroachden.planner.solver.schedule.Objectives;
import com.cocroachden.planner.solver.schedule.SchedulePlanBuilder;
import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
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
      log.debug("Interrupting running solver thread.");
      this.solverThread.interrupt();
    }
    if (this.solutionCb != null) {
      log.debug("Calling stop on an existing solution callback.");
      this.solutionCb.stopSearch();
    }
    Loader.loadNativeLibraries();
    var model = new CpModel();
    var objectives = new Objectives();
    var schedulePlan = SchedulePlanBuilder.create(schedulePlanConfiguration, model);
    constraintRequests.forEach(request -> constraintApplier.apply(schedulePlan, model, objectives, request));
    this.cpSolver = new CpSolver();
    this.cpSolver.getParameters().setLinearizationLevel(0);
    this.cpSolver.getParameters().setNumWorkers(8);
    this.cpSolver.getParameters().setRelativeGapLimit(0.05);
    this.cpSolver.getParameters().setLogSearchProgress(true);
    model.minimize(objectives.getObjectiveAsExpression());
    this.solutionCb = new ScheduleSolutionCb(
        fluxSink,
        schedulePlan
    );
    this.solverThread = new Thread(() -> {
      var status = this.cpSolver.solve(model, this.solutionCb);
      if (status == CpSolverStatus.INFEASIBLE) {
        fluxSink.accept(
            new ScheduleResultDTO(
                SolutionStatus.INFEASIBLE,
                0d,
                0,
                new HashMap<>()
            ));
      }
      if (status == CpSolverStatus.MODEL_INVALID) {
        fluxSink.accept(
            new ScheduleResultDTO(
                SolutionStatus.MODEL_INVALID,
                0d,
                0,
                new HashMap<>()
            ));
      }
      log.info(model.modelStats());
      log.info(model.validate());
      log.info("Status: {}, Solutions: {}", status, this.solutionCb.getCurrentSolutionCount());
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
