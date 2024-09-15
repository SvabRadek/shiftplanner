package com.cocroachden.planner.solver.service;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSolutionDTO;

import java.util.function.Consumer;

public interface Solver {
    void solve(
            SolverProblemConfiguration configuration,
            Consumer<SolverSolutionDTO> callback,
            SolverOptions solverOptions
    );
    void stop();
}
