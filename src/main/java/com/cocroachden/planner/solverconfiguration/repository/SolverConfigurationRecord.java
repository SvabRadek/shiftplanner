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
import org.hibernate.annotations.Fetch;
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
    @Setter
    private String owningUser;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant lastUpdated;
    @Setter
    private LocalDate startDate;
    @Setter
    private LocalDate endDate;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ConstraintRecord> constraintRecords = new ArrayList<>();
    @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EmployeeAssignmentRecord> employeeAssignments = new ArrayList<>();

    public void addConstraint(ConstraintRecord constraint) {
        this.constraintRecords.add(constraint);
    }

    public void addAssignment(EmployeeAssignmentRecord assignment) {
        this.employeeAssignments.add(assignment);
    }

    public SolverConfigurationId getId() {
        return SolverConfigurationId.from(id);
    }
}
