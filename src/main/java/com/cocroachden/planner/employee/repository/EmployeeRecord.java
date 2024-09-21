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
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ConstraintRecord> constraints = new ArrayList<>();
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<EmployeeAssignmentRecord> assignments = new ArrayList<>();

    public void addConstraint(ConstraintRecord constraint) {
        if (constraint == null) return;
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
}