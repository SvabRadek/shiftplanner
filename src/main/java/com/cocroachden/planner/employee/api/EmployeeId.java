package com.cocroachden.planner.employee.api;

import dev.hilla.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
@EqualsAndHashCode
public class EmployeeId implements Serializable {
  @Serial
  private static final long serialVersionUID = -3136703610267018121L;
  @Column(name = "employee_id", nullable = false)
  @Nonnull
  private Long id;

  @Override
  public String toString() {
    return id.toString();
  }
}
