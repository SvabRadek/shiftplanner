package com.cocroachden.planner.employee.repository;

import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;
import dev.hilla.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "employee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public final class EmployeeRecord {

    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "employee_id"))
    private EmployeeId id;
    private String firstName;
    private String lastName;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConstraintRecord> requests = new ArrayList<>();
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeAssignmentRecord> assignments = new ArrayList<>();

    public EmployeeRecord(@Nonnull EmployeeId id, @Nonnull String firstName, @Nonnull String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public EmployeeRecord(@Nonnull String firstName, @Nonnull String lastName) {
        this.id = EmployeeId.random();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EmployeeRecord other) {
            return other.id.equals(this.id);
        }
        return false;
    }
}