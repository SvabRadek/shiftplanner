package com.cocroachden.planner.solver.service.solution;


import com.cocroachden.planner.solver.WorkShifts;

import java.time.LocalDate;

public record SolutionWorkDay(
    LocalDate date,
    WorkShifts assignedShift
) {

}
