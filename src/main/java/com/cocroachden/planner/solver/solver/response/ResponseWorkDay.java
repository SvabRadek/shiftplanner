package com.cocroachden.planner.solver.solver.response;

import com.cockroachden.shiftschedulerback.model.schedule.WorkShifts;

import java.time.LocalDate;

public record ResponseWorkDay(
    LocalDate date,
    WorkShifts assignedShift
    ) {
  
}
