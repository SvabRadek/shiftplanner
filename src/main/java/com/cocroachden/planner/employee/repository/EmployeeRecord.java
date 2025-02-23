package com.cocroachden.planner.employee.repository;

import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "employee")
@NoArgsConstructor
@Getter
@Accessors(chain = true)
public final class EmployeeRecord {

    @Setter
    @Id
    private String id;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String owningUser;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ConstraintRecord> constraints = new ArrayList<>();
    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EmployeeAssignmentRecord> assignments = new ArrayList<>();

    public void addConstraint(ConstraintRecord constraint) {
        constraints.add(constraint);
    }

    public void addAssignment(EmployeeAssignmentRecord assignment) {
        if (assignment == null) return;
        assignments.add(assignment);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EmployeeRecord other) {
            return other.id.equals(this.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public EmployeeId getId() {
        return EmployeeId.from(id);
    }
}