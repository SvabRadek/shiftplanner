package com.cocroachden.planner.lib;

import lombok.Getter;
@Getter
public abstract class AbstractIdentity {
  protected String id;

  private AbstractIdentity() {}

  protected AbstractIdentity(String id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  @Override
  public String toString() {
    return this.id;
  }
}
