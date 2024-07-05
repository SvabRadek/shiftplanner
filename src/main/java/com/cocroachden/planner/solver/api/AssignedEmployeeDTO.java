package com.cocroachden.planner.solver.api;

import com.cocroachden.planner.employee.api.EmployeeDTO;
import com.cocroachden.planner.solver.repository.EmployeeAssignment;
import dev.hilla.Nonnull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AssignedEmployeeDTO {

  public static AssignedEmployeeDTO from(EmployeeAssignment assignment) {
    return new AssignedEmployeeDTO(
        EmployeeDTO.from(assignment.getEmployee()),
        assignment.getIndex(),
        assignment.getWeight()
    );
  }

  private @Nonnull EmployeeDTO employee;
  private @Nonnull Integer index;
  private @Nonnull Integer weight;

}
