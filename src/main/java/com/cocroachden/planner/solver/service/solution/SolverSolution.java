package com.cocroachden.planner.solver.service.solution;


import com.cocroachden.planner.employee.EmployeeId;

import java.util.List;
import java.util.Map;

public record SolverSolution(
    Map<EmployeeId, List<SolutionWorkDay>> workdays
) {
}

