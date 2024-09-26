package com.cocroachden.planner.solver.service.testimplementation;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.solver.SolverOptions;

import java.util.function.Consumer;

@FunctionalInterface
public interface TestSolverCallback {
    void solve(SolverProblemConfiguration configuration, Consumer<SolverSolutionDTO> callback, SolverOptions solverOptions);
}
