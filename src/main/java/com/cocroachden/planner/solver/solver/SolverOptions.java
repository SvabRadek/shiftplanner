package com.cocroachden.planner.solver.solver;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SolverOptions {
    private Integer solvingLimitInSec;
}
