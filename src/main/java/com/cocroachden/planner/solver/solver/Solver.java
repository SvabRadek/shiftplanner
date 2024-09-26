package com.cocroachden.planner.solver.solver;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSolutionDTO;

import java.util.function.Consumer;

public interface Solver {
    void start(
            SolverProblemConfiguration problemConfiguration,
            Consumer<SolverSolutionDTO> callback,
            SolverOptions options
    );

    void stop();
}
