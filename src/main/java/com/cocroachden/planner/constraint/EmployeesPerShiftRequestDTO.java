package com.cocroachden.planner.constraint;


import com.cocroachden.planner.solver.constraints.specific.employeespershift.request.EmployeesPerShiftRequest;
import com.cocroachden.planner.solver.api.WorkShifts;
import dev.hilla.Nonnull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeesPerShiftRequestDTO implements ConstraintRequestDTO {
  public static EmployeesPerShiftRequestDTO from(String id, EmployeesPerShiftRequest request) {
    return new EmployeesPerShiftRequestDTO(
        id,
        request.getShift(),
        request.getHardMin(),
        request.getSoftMin(),
        request.getMinPenalty(),
        request.getSoftMax(),
        request.getMaxPenalty(),
        request.getHardMax()
    );
  }
  @Nonnull
  private final ConstraintType type = ConstraintType.EMPLOYEES_PER_SHIFT;
  @Nonnull
  @Setter
  private String id;
  @Nonnull
  private WorkShifts targetShift;
  @Nonnull
  private Integer hardMin;
  @Nonnull
  private Integer softMin;
  @Nonnull
  private Integer minPenalty;
  @Nonnull
  private Integer softMax;
  @Nonnull
  private Integer maxPenalty;
  @Nonnull
  private Integer hardMax;
}
