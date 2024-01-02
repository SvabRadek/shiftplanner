package com.cocroachden.planner.lib;

public class ConstraintRequestId extends AbstractIdentity {
  public ConstraintRequestId(String id) {
    super(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ConstraintRequestId other) {
      return other.id.equals(id);
    }
    return false;
  }
}
