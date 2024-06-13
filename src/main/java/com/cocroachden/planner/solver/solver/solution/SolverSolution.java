package com.cocroachden.planner.solver.solver.solution;


import com.cocroachden.planner.core.identity.WorkerId;

import java.util.List;
import java.util.Map;

public record SolverSolution(
    Map<WorkerId, List<SolutionWorkDay>> workdays
) {
}

