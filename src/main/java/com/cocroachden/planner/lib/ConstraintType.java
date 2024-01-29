package com.cocroachden.planner.lib;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstraintType {
  SPECIFIC_SHIFT_REQUEST,
  SHIFT_PER_SCHEDULE,
  CONSECUTIVE_WORKING_DAYS,
  ONE_SHIFT_PER_DAY,
  SHIFT_FOLLOW_UP_RESTRICTION,
  SHIFT_PATTERN_POSITIVE_CONSTRAINT,
  WORKERS_PER_SHIFT;
}
