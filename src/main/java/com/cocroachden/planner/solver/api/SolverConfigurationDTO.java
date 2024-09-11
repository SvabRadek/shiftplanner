package com.cocroachden.planner.solver.api;

import com.cocroachden.planner.constraint.ConstraintMapper;
import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.solver.repository.EmployeeAssignment;
import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SolverConfigurationDTO {
  public static SolverConfigurationDTO from(SolverConfigurationRecord record) {
    return new SolverConfigurationDTO(
        record.getId().getId(),
        record.getName(),
        record.getCreatedAt(),
        record.getLastUpdated(),
        record.getStartDate(),
        record.getEndDate(),
        record.getEmployeeAssignments().stream()
            .sorted(Comparator.comparing(EmployeeAssignment::getIndex))
            .map(AssignedEmployeeDTO::from)
            .toList(),
        record.getConstraintRequestRecords().stream()
            .map(ConstraintMapper::fromRecord)
            .toList()
    );
  }

  @Nonnull
  @Setter
  private String id;
  @Nonnull
  private String name;
  @Nonnull
  private Instant createdAt;
  @Nonnull
  private Instant lastUpdated;
  @Nonnull
  private LocalDate startDate;
  @Nonnull
  private LocalDate endDate;
  @Nonnull
  private List<@Nonnull AssignedEmployeeDTO> employees;
  @Nonnull
  private List<@Nonnull ConstraintRequestDTO> constraints;
}
