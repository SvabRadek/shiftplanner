package com.cocroachden.planner.solver.repository;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.employee.api.EmployeeId;
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
  @ElementCollection
  @OrderColumn(name = "list_index")
  private List<EmployeeId> employees;
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ConstraintRequestRecord> constraintRequestRecords = new ArrayList<>();

  public void setConstraintRequestRecords(List<ConstraintRequestRecord> constraintRequestRecords) {
    this.constraintRequestRecords.clear();
    if (constraintRequestRecords != null) {
      this.constraintRequestRecords.addAll(constraintRequestRecords);
    }
  }
}
