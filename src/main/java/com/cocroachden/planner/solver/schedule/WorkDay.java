package com.cocroachden.planner.solver.schedule;


import com.cocroachden.planner.lib.WorkerId;
import com.google.ortools.sat.BoolVar;
import org.checkerframework.common.value.qual.BoolVal;

import java.time.LocalDate;
import java.util.List;

public record WorkDay(
    WorkerId owner,
    LocalDate date,
    BoolVar offShiftAssignment,
    BoolVar dayShiftAssignment,
    BoolVar nightShiftAssignment
) {
  public List<BoolVar> getShifts(WorkShifts index) {
    switch (index) {
      case OFF -> {
        return List.of(offShiftAssignment);
      }
      case DAY -> {
        return List.of(dayShiftAssignment);
      }
      case NIGHT -> {
        return List.of(nightShiftAssignment);
      }
      case WORKING_SHIFTS -> {
        return List.of(dayShiftAssignment, nightShiftAssignment);
      }
      default -> throw new RuntimeException("Invalid shift index: " + index);
    }
  }
}
