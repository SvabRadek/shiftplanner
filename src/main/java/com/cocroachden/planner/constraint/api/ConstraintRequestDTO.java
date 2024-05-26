package com.cocroachden.planner.constraint.api;

import com.cocroachden.planner.lib.ConstraintType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.hilla.Nonnull;

import java.util.UUID;

@JsonDeserialize(using = ConstraintRequestDTODeserializer.class)
public interface ConstraintRequestDTO {
  @Nonnull UUID getId();

  @Nonnull ConstraintType getType();
}
