package com.cocroachden.planner.configuration;

import com.cocroachden.planner.lib.WorkerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
  private LocalDate startDate;
  private LocalDate endDate;
  @ElementCollection
  @OrderColumn(name = "list_index")
  private List<WorkerId> workers;
  @OneToMany
  private List<ConstraintRequestRecord> constraintRequests;
}
