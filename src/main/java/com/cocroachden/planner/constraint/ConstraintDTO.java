package com.cocroachden.planner.constraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.hilla.Nonnull;

@JsonDeserialize(using = ConstraintDTODeserializer.class)
public interface ConstraintDTO {
  @Nonnull
  String getId();

  void setId(@Nonnull String id);

  @Nonnull
  ConstraintType getType();
}
