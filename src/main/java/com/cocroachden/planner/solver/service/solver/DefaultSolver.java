package com.cocroachden.planner.solver.service.solver;


import com.cocroachden.planner.solver.SolutionStatus;
import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.constraints.GenericConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.ConsecutiveWorkingDaysConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.EmployeeShiftRequestConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.EmployeesPerShiftConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.evenshiftdistribution.EvenShiftDistributionConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.ShiftFollowUpConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.ShiftPatternConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.OneShiftPerDayConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.request.OneShiftPerDayConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.ShiftsPerScheduleConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.teamassignment.TeamAssignmentConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.TripleShiftConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.weekends.WeekendRequestsApplier;
import com.cocroachden.planner.solver.service.solver.schedule.SchedulePlan;
import com.cocroachden.planner.solver.service.solution.SolverSolutionCallback;
import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class DefaultSolver implements Solver {
    private final GenericConstraintApplier constraintApplier = new GenericConstraintApplier(
            List.of(
                    new ConsecutiveWorkingDaysConstraintApplier(),
                    new EmployeeShiftRequestConstraintApplier(),
                    new EmployeesPerShiftConstraintApplier(),
                    new EvenShiftDistributionConstraintApplier(),
                    new ShiftFollowUpConstraintApplier(),
                    new ShiftPatternConstraintApplier(),
                    new OneShiftPerDayConstraintApplier(),
                    new ShiftsPerScheduleConstraintApplier(),
                    new TeamAssignmentConstraintApplier(),
                    new TripleShiftConstraintApplier(),
                    new WeekendRequestsApplier()
            )
    );
    private SolverSolutionCallback solverSolutionCallback;

    public void solve(
            SolverProblemConfiguration solverProblemConfiguration,
            Consumer<SolverSolutionDTO> solutionCallback,
            SolverOptions options
    ) {
        if (this.solverSolutionCallback != null) {
            log.debug("Calling stop on an existing solution callback.");
            this.solverSolutionCallback.stopSearch();
        }
        Loader.loadNativeLibraries();
        var model = new CpModel();
        var objectives = new SolutionObjectives();
        var schedulePlan = new SchedulePlan(solverProblemConfiguration, model);
        solverProblemConfiguration.solverConstraints().forEach(request ->
                constraintApplier.apply(schedulePlan, model, objectives, request)
        );
        if (solverProblemConfiguration.solverConstraints().stream().noneMatch(c -> c instanceof OneShiftPerDayConstraint)) {
            //Apply constraint to assign only one shift per day. It is so basic constraint it feels useless to have it in configuration
            constraintApplier.apply(schedulePlan, model, objectives, new OneShiftPerDayConstraint());
        }
        var cpSolver = new CpSolver();
        cpSolver.getParameters().setLinearizationLevel(0);
        cpSolver.getParameters().setRelativeGapLimit(0.05);
        cpSolver.getParameters().setLogSearchProgress(true);
        cpSolver.getParameters().setEnumerateAllSolutions(true);
        cpSolver.getParameters().setMaxTimeInSeconds(options.getSolvingLimitInSec());
        model.minimize(objectives.getObjectiveAsExpression());
        this.solverSolutionCallback = new SolverSolutionCallback(
                solutionCallback,
                schedulePlan
        );
        var status = cpSolver.solve(model, this.solverSolutionCallback);
        var markerSolution = switch (status) {
            case INFEASIBLE -> new SolverSolutionDTO(
                    SolutionStatus.INFEASIBLE,
                    0d,
                    0,
                    new HashMap<>()
            );
            case MODEL_INVALID -> new SolverSolutionDTO(
                    SolutionStatus.MODEL_INVALID,
                    0d,
                    0,
                    new HashMap<>()
            );
            case OPTIMAL -> new SolverSolutionDTO(
                    SolutionStatus.OPTIMAL,
                    0d,
                    0,
                    new HashMap<>()
            );
            default -> null;
        };
        if (markerSolution != null) {
            solutionCallback.accept(markerSolution);
        }
        log.info(model.modelStats());
        log.info(model.validate());
        log.info("Status: {}, Solutions: {}", status, this.solverSolutionCallback.getCurrentSolutionCount());
        log.debug("Statistics");
        log.debug("  conflicts: {}", cpSolver.numConflicts());
        log.debug("  branches : {}", cpSolver.numBranches());
        log.debug("  wall time: {}", cpSolver.wallTime());
        log.info("Starting solver thread...");
    }

    @Override
    public void stop() {
        if (this.solverSolutionCallback != null) {
            this.solverSolutionCallback.stopSearch();
        }
    }
}
