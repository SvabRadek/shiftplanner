package com.cocroachden.planner.constraint.api;

import com.cocroachden.planner.lib.ConstraintRequestDTODeserializer;
import com.cocroachden.planner.lib.ConstraintType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

@JsonDeserialize(using = ConstraintRequestDTODeserializer.class)
public interface ConstraintRequestDTO {
  UUID getId();

  ConstraintType getType();
}
