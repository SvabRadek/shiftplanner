package com.cocroachden.planner.constraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.hilla.Nonnull;

@JsonDeserialize(using = ConstraintRequestDTODeserializer.class)
public interface ConstraintRequestDTO {
  @Nonnull
  String getId();

  void setId(@Nonnull String id);

  @Nonnull
  ConstraintType getType();
}
