package com.cocroachden.planner.constraint.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.hilla.Nonnull;

import java.util.UUID;

@JsonDeserialize(using = ConstraintRequestDTODeserializer.class)
public interface ConstraintRequestDTO {
  @Nonnull
  String getId();

  @Nonnull
  ConstraintType getType();
}
