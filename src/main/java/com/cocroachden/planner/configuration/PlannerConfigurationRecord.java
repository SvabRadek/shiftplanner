package com.cocroachden.planner.configuration;

import com.cocroachden.planner.lib.WorkerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "planner_configuration")
@Table(name = "planner_configurations")
public class PlannerConfigurationRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @CreationTimestamp
  private Instant createdAt;
  @UpdateTimestamp
  private Instant lastUpdated;
  private LocalDate startDate;
  private LocalDate endDate;
  @ElementCollection
  @OrderColumn(name = "list_index")
  private List<WorkerId> workers;
  @OneToMany(cascade = CascadeType.ALL)
  private List<ConstraintRequestRecord> constraintRequests;
}
