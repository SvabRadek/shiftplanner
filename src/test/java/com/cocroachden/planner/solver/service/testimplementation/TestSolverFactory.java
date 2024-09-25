package com.cocroachden.planner.solver.service.testimplementation;

import com.cocroachden.planner.solver.service.solver.Solver;
import com.cocroachden.planner.solver.service.solver.SolverFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TestSolverFactory implements SolverFactory {

    private Solver solver;

    @Override
    public Solver instantiate() {
        if (solver == null) {
            throw new RuntimeException("No solver was set!");
        }
        return solver;
    }
}
