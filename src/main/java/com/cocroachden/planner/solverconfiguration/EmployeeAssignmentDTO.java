package com.cocroachden.planner.solverconfiguration;

import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;
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

  private @Nonnull EmployeeId employee;
  private @Nonnull Integer index;
  private @Nonnull Integer weight;

}
