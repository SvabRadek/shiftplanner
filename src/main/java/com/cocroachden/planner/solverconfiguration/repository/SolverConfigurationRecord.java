package com.cocroachden.planner.solverconfiguration.repository;

import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "solver_configuration")
@Accessors(chain = true)
public class SolverConfigurationRecord {
    @Id
    @Setter
    private String id;
    @Setter
    private String name;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant lastUpdated;
    @Setter
    private LocalDate startDate;
    @Setter
    private LocalDate endDate;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConstraintRecord> constraintRecords = new ArrayList<>();
    @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeAssignmentRecord> employeeAssignments = new ArrayList<>();

    public void addConstraints(List<ConstraintRecord> constraints) {
        constraints.forEach(this::addConstraint);
    }

    public void addConstraint(ConstraintRecord constraint) {
        if (constraint == null) return;
        this.constraintRecords.add(constraint);
    }

    public void addAssignments(List<EmployeeAssignmentRecord> assignments) {
        assignments.forEach(this::addAssignment);
    }

    public void addAssignment(EmployeeAssignmentRecord assignment) {
        if (assignment == null) return;
        this.employeeAssignments.add(assignment);
    }
}
