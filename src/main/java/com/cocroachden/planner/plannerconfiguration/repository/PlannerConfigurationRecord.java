package com.cocroachden.planner.plannerconfiguration.repository;

import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "planner_configurations")
public class PlannerConfigurationRecord {

  public static PlannerConfigurationRecord from(
      UUID id,
      PlannerConfigurationDTO dto
  ) {
    return new PlannerConfigurationRecord(
        id,
        dto.getName(),
        dto.getCreatedAt(),
        dto.getLastUpdated(),
        dto.getStartDate().toDate(),
        dto.getEndDate().toDate(),
        dto.getWorkers(),
        dto.getConstraintRequestInstances().stream()
            .map(link -> new ConfigurationRequestLink(
                link.getRequestType(),
                link.getRequestId()
            )).toList()
    );
  }

  public PlannerConfigurationRecord(
      String name,
      LocalDate startDate,
      LocalDate endDate,
      List<WorkerId> workers,
      List<ConfigurationRequestLink> constraintRequestInstances
  ) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.workers = workers;
    this.constraintRequestInstances = constraintRequestInstances;
  }

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
  private List<WorkerId> workers;
  @ElementCollection
  private List<ConfigurationRequestLink> constraintRequestInstances;
}
