package com.cocroachden.planner.solver.repository;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "SolverConfiguration")
@Table(name = "solver_configuration")
public class SolverConfigurationRecord {
  @Id
  @GeneratedValue
  private UUID id;
  private String name;
  @CreationTimestamp
  private Instant createdAt;
  @UpdateTimestamp
  private Instant lastUpdated;
  private LocalDate startDate;
  private LocalDate endDate;
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ConstraintRequestRecord> constraintRequestRecords = new ArrayList<>();
  @ManyToMany
  @JoinTable(
      name = "solver_configuration_employees",
      joinColumns = @JoinColumn(name = "solver_configuration_id"),
      inverseJoinColumns = @JoinColumn(name = "employee_id")
  )
  private List<EmployeeRecord> employees = new ArrayList<>();

  public void setConstraintRequestRecords(List<ConstraintRequestRecord> constraintRequestRecords) {
    this.constraintRequestRecords.clear();
    if (constraintRequestRecords != null) {
      this.constraintRequestRecords.addAll(constraintRequestRecords);
    }
  }

  public void setEmployees(List<EmployeeRecord> employees) {
    this.employees.clear();
    if (employees != null) {
      this.employees.addAll(employees);
    }
  }
}
