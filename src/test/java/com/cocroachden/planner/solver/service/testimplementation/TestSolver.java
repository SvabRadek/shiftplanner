package com.cocroachden.planner.solver.service.testimplementation;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.service.Solver;
import com.cocroachden.planner.solver.service.SolverOptions;

import java.util.function.Consumer;

public class TestSolver implements Solver {
    @Override
    public void solve(SolverProblemConfiguration configuration, Consumer<SolverSolutionDTO> callback, SolverOptions solverOptions) {
        callback.accept(new SolverSolutionDTO());
    }

    @Override
    public void stop() {

    }
}
