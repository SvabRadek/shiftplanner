package com.cocroachden.planner.solver.service.solver;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SolverOptions {
    private Integer solvingLimitInSec;
}
