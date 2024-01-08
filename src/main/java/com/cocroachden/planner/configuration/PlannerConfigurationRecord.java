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
  @Id
  @GeneratedValue
  @Setter
  private UUID id;
  @NotEmpty(message = "Konfigurace musi mit nejake jmeno.")
  private String name;
  @CreationTimestamp
  private Instant createdAt;
  @UpdateTimestamp
  private Instant lastUpdated;
  @Nonnull
  private LocalDate startDate;
  @Nonnull
  private LocalDate endDate;
  @ElementCollection
  @OrderColumn(name = "list_index")
  @Nonnull
  private List<@Nonnull WorkerId> workers;
  @ElementCollection
  @Nonnull
  private List<@Nonnull UUID> constraintRequestInstances;
}
