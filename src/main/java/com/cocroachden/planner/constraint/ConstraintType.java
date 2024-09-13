package com.cocroachden.planner.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstraintType {
  EMPLOYEE_SHIFT_REQUEST,
  SHIFT_PER_SCHEDULE,
  CONSECUTIVE_WORKING_DAYS,
  ONE_SHIFT_PER_DAY,
  SHIFT_FOLLOW_UP_RESTRICTION,
  SHIFT_PATTERN_CONSTRAINT,
  EMPLOYEES_PER_SHIFT,
  TRIPLE_SHIFTS_CONSTRAINT,
  TEAM_ASSIGNMENT,
  WEEKEND_REQUEST,
  EVEN_SHIFT_DISTRIBUTION
}
