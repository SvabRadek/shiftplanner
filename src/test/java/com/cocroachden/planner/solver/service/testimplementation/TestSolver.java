package com.cocroachden.planner.solver.service.testimplementation;

import com.cocroachden.planner.solver.SolverProblemConfiguration;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.solver.Solver;
import com.cocroachden.planner.solver.solver.SolverOptions;
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
    public void start(SolverProblemConfiguration problemConfiguration, Consumer<SolverSolutionDTO> callback, SolverOptions options) {
        testCallback.solve(problemConfiguration, callback, options);
    }

    @Override
    public void stop() {
        this.testStopCallback.run();
    }


}
