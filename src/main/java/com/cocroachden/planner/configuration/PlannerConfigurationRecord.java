package com.cocroachden.planner.configuration;

import com.cocroachden.planner.lib.WorkerId;
import dev.hilla.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

  public static PlannerConfigurationRecord from(PlannerConfigurationService.PlannerConfigurationResponse response) {
    return new PlannerConfigurationRecord(
        UUID.randomUUID(),
        response.getName(),
        response.getCreatedAt(),
        response.getLastUpdated(),
        response.getStartDate(),
        response.getEndDate(),
        response.getWorkers(),
        response.getConstraintRequestInstances()
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
  private List<UUID> constraintRequestInstances;
}
