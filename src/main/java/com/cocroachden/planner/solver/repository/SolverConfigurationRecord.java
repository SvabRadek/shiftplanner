package com.cocroachden.planner.solver.repository;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.solver.SolverConfigurationId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "solver_configuration")
public class SolverConfigurationRecord {
  @EmbeddedId
  @AttributeOverride(name = "id", column = @Column(name = "solver_configuration_id"))
  private SolverConfigurationId id;
  private String name;
  @CreationTimestamp
  private Instant createdAt;
  @UpdateTimestamp
  private Instant lastUpdated;
  private LocalDate startDate;
  private LocalDate endDate;
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ConstraintRequestRecord> constraintRequestRecords = new ArrayList<>();
  @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EmployeeAssignment> employeeAssignments = new ArrayList<>();

  public SolverConfigurationRecord(
          SolverConfigurationId id,
          String name,
          LocalDate startDate,
          LocalDate endDate
  ) {
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public void setConstraintRequestRecords(List<ConstraintRequestRecord> constraintRequestRecords) {
    this.constraintRequestRecords.clear();
    if (constraintRequestRecords != null) {
      this.constraintRequestRecords.addAll(constraintRequestRecords);
    }
  }

  public void setEmployeeAssignments(List<EmployeeAssignment> employees) {
    this.employeeAssignments.clear();
    if (employees != null) {
      this.employeeAssignments.addAll(employees);
    }
  }
}
