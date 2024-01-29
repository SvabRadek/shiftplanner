package com.cocroachden.planner.plannerconfiguration.repository;

import com.cocroachden.planner.lib.WorkerId;
import com.cocroachden.planner.plannerconfiguration.ConfigurationRequestLinkDTO;
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
@Entity(name = "planner_configuration")
@Table(name = "planner_configurations")
public class PlannerConfigurationRecord {

  public static PlannerConfigurationRecord from(PlannerConfigurationDTO response) {
    return new PlannerConfigurationRecord(
        UUID.randomUUID(),
        response.getName(),
        response.getCreatedAt(),
        response.getLastUpdated(),
        response.getStartDate().toDate(),
        response.getEndDate().toDate(),
        response.getWorkers(),
        response.getConstraintRequestInstances().stream()
            .map(link -> new ConfigurationRequestLink(
                link.getRequestType(),
                link.getRequestId()
            )).toList()
    );
  }

  @Id
  @GeneratedValue
  @Setter
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
