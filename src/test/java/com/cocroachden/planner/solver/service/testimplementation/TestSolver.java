package com.cocroachden.planner.solver.service.testimplementation;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.service.solver.Solver;
import com.cocroachden.planner.solver.service.solver.SolverOptions;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Consumer;

@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class TestSolver implements Solver {

    private TestSolverCallback testCallback;
    private Runnable testStopCallback;

    public TestSolver(TestSolverCallback testCallback) {
        this.testCallback = testCallback;
        this.testStopCallback = null;
    }

    @Override
    public void solve(SolverProblemConfiguration configuration, Consumer<SolverSolutionDTO> callback, SolverOptions solverOptions) {
        testCallback.solve(configuration, callback, solverOptions);
    }

    @Override
    public void stop() {
        this.testStopCallback.run();
    }


}
