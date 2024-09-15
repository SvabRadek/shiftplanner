package com.cocroachden.planner.solver.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SolverOptions {
    private Integer solvingLimitInSec;
}
