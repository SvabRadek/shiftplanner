package com.cocroachden.planner.employee;

import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class EmployeeDTO {
  @JsonProperty
  private @Nonnull String id;
  private @Nonnull String firstName;
  private @Nonnull String lastName;

  public static EmployeeDTO from(EmployeeRecord record) {
    return new EmployeeDTO(
        record.getId(),
        record.getFirstName(),
        record.getLastName()
    );
  }

  @JsonIgnore
  public @Nonnull EmployeeId getId() {
    return EmployeeId.from(id);
  }
}
