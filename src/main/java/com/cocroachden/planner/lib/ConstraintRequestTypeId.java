package com.cocroachden.planner.lib;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConstraintRequestTypeId extends AbstractIdentity {
  @Serial
  private static final long serialVersionUID = 8930579657252155114L;
  public ConstraintRequestTypeId(String id) {
    super(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ConstraintRequestTypeId other) {
      return other.id.equals(id);
    }
    return false;
  }
}
