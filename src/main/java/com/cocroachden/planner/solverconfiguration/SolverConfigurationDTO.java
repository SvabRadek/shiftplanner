package com.cocroachden.planner.solverconfiguration;

import com.cocroachden.planner.constraint.ConstraintDTO;
import com.cocroachden.planner.constraint.mapping.ConstraintMapper;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SolverConfigurationDTO {
  public static SolverConfigurationDTO from(SolverConfigurationRecord record) {
    return new SolverConfigurationDTO(
        record.getId(),
        record.getName(),
        record.getCreatedAt(),
        record.getLastUpdated(),
        record.getStartDate(),
        record.getEndDate(),
        record.getEmployeeAssignments().stream()
            .sorted(Comparator.comparing(EmployeeAssignmentRecord::getIndex))
            .map(EmployeeAssignmentDTO::from)
            .toList(),
        record.getConstraintRecords().stream()
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
  private List<@Nonnull EmployeeAssignmentDTO> employees;
  @Nonnull
  private List<@Nonnull ConstraintDTO> constraints;
}
