package com.cocroachden.planner.solver.solver;

import org.springframework.stereotype.Service;

@Service
public class DefaultSolverFactory implements SolverFactory {
    @Override
    public Solver instantiate() {
        return new DefaultSolver();
    }
}
