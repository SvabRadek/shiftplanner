package com.cocroachden.planner.employee.api;

import com.cocroachden.planner.employee.repository.EmployeeRecord;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class EmployeeDTO {
  private @Nonnull Long id;
  private @Nonnull String firstName;
  private @Nonnull String lastName;

  public static EmployeeDTO from(EmployeeRecord record) {
    return new EmployeeDTO(
        record.getId(),
        record.getFirstName(),
        record.getLastName()
    );
  }
}
