package com.cocroachden.planner.solver.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WorkShifts {
  OFF(0, "V"),
  DAY(1, "D"),
  NIGHT(2, "N"),
  WORKING_SHIFTS(3, "W");
  private final Integer code;
  private final String symbol;
}
