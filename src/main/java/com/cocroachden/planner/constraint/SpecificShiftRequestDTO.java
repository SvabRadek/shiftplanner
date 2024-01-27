package com.cocroachden.planner.constraint;

import com.cocroachden.planner.lib.StupidDate;
import com.cocroachden.planner.solver.schedule.WorkShifts;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SpecificShiftRequestDTO {
  public static final String TYPE = "SpecificShiftRequest";
  @Nonnull
  private String type;
  @Nonnull
  private String owner;
  @Nonnull
  private StupidDate date;
  @Nonnull
  private WorkShifts requestedShift;
}
