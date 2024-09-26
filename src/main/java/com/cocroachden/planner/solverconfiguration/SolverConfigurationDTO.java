package com.cocroachden.planner.solverconfiguration;

import com.cocroachden.planner.constraint.ConstraintDTO;
import com.cocroachden.planner.constraint.mapping.ConstraintMapper;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.hilla.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
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

    public SolverConfigurationDTO(
            @Nonnull SolverConfigurationId id,
            @Nonnull String name,
            @Nonnull Instant createdAt,
            @Nonnull Instant lastUpdated,
            @Nonnull LocalDate startDate,
            @Nonnull LocalDate endDate,
            @Nonnull List<@Nonnull EmployeeAssignmentDTO> employees,
            @Nonnull List<@Nonnull ConstraintDTO> constraints
    ) {
        this.id = id.getId();
        this.name = name;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employees = employees;
        this.constraints = constraints;
    }

    @Nonnull
    @Setter
    @JsonProperty
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
    private List<@Nonnull EmployeeAssignmentDTO> employees = new ArrayList<>();
    @Nonnull
    private List<@Nonnull ConstraintDTO> constraints = new ArrayList<>();

    @JsonIgnore
    public @Nonnull SolverConfigurationId getId() {
        return SolverConfigurationId.from(id);
    }
}
