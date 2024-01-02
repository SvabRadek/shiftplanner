package com.cocroachden.planner.solver.solver.response;


import com.cocroachden.planner.solver.schedule.WorkShifts;

import java.time.LocalDate;

public record ResponseWorkDay(
    LocalDate date,
    WorkShifts assignedShift
    ) {
  
}
