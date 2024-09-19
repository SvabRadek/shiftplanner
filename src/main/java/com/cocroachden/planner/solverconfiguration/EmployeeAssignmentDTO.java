package com.cocroachden.planner.solverconfiguration;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeeAssignmentDTO {

  public static EmployeeAssignmentDTO from(EmployeeAssignmentRecord assignment) {
    return new EmployeeAssignmentDTO(
        assignment.getEmployee().getId(),
        assignment.getIndex(),
        assignment.getWeight()
    );
  }

  @JsonProperty
  private @Nonnull String employeeId;
  private @Nonnull Integer index;
  private @Nonnull Integer weight;

  public EmployeeAssignmentDTO(@Nonnull EmployeeId employeeId, @Nonnull Integer index, @Nonnull Integer weight) {
    this.employeeId = employeeId.getId();
    this.index = index;
    this.weight = weight;
  }

  @JsonIgnore
  public @Nonnull EmployeeId getEmployeeId() {
    return EmployeeId.from(employeeId);
  }
}
