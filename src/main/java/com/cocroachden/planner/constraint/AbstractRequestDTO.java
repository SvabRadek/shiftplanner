package com.cocroachden.planner.constraint;

import com.cocroachden.planner.lib.ConstraintType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public abstract class AbstractRequestDTO {
  private UUID id;
  private ConstraintType constraintType;
}
