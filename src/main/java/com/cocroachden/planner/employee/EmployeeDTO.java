package com.cocroachden.planner.employee;

import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class EmployeeDTO {
  private @Nonnull String id;
  private @Nonnull String firstName;
  private @Nonnull String lastName;

  public static EmployeeDTO from(EmployeeRecord record) {
    return new EmployeeDTO(
        record.getId().getId(),
        record.getFirstName(),
        record.getLastName()
    );
  }
}
