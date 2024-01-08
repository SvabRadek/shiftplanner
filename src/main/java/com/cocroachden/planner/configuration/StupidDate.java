package com.cocroachden.planner.configuration;


import dev.hilla.Nonnull;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class StupidDate {

  public static StupidDate fromDate(LocalDate date) {
    return new StupidDate(
        date.getDayOfMonth(),
        date.getMonth().getValue(),
        date.getYear()
    );
  }

  @Nonnull
  @Max(31)
  private Integer day;
  @Nonnull
  @Max(12)
  private Integer month;
  @Nonnull
  private Integer year;

  public LocalDate toDate() {
    return LocalDate.of(year, month, day);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StupidDate that = (StupidDate) o;
    return Objects.equals(day, that.day) && Objects.equals(
        month,
        that.month
    ) && Objects.equals(year, that.year);
  }

  @Override
  public int hashCode() {
    return Objects.hash(day, month, year);
  }

  @Override
  public String toString() {
    return "%s/%s/%s".formatted(day, month, year);
  }
}
