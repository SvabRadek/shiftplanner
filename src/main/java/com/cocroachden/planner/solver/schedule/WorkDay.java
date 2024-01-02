package com.cocroachden.planner.solver.schedule;


import com.cocroachden.planner.lib.WorkerId;
import com.google.ortools.sat.BoolVar;

import java.time.LocalDate;

public record WorkDay(
    WorkerId owner,
    LocalDate date,
    BoolVar offShiftAssignment,
    BoolVar dayShiftAssignment,
    BoolVar nightShiftAssignment
) {
  public BoolVar getShift(WorkShifts index) {
    switch (index) {
      case OFF -> {
        return offShiftAssignment;
      }
      case DAY -> {
        return dayShiftAssignment;
      }
      case NIGHT -> {
        return nightShiftAssignment;
      }
      default -> throw new RuntimeException("Invalid shift index: " + index);
    }
  }
}
