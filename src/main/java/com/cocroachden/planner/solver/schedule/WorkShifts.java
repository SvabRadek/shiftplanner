package com.cocroachden.planner.solver.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WorkShifts {
  OFF(0, "V"),
  DAY(1, "D"),
  NIGHT(2, "N"),
  NOT_DAY(4, "!D"),
  NOT_NIGHT(5, "!N"),
  WORKING_SHIFTS(3, "W"),
  ANY(6, " ");
  private final Integer code;
  private final String symbol;

  public boolean isSameAs(WorkShifts other) {
    return switch (other) {
      case DAY -> this.equals(other) || this.equals(WORKING_SHIFTS);
      case NIGHT -> this.equals(other) || this.equals(WORKING_SHIFTS);
      case WORKING_SHIFTS ->
          this.equals(other)
              || this.equals(DAY)
              || this.equals(NIGHT);
      default -> this.equals(other);
    };
  }
}
