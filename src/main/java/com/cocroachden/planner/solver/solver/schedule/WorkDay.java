package com.cocroachden.planner.solver.solver.schedule;


import com.cocroachden.planner.core.identity.WorkerId;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.google.ortools.sat.BoolVar;

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
      case NOT_NIGHT -> {
        return List.of(dayShiftAssignment, offShiftAssignment);
      }
      case NOT_DAY -> {
        return List.of(nightShiftAssignment, offShiftAssignment);
      }
      case ANY -> {
        return List.of(dayShiftAssignment, nightShiftAssignment, offShiftAssignment);
      }
      default -> throw new RuntimeException("Invalid shift index: " + index);
    }
  }
}
