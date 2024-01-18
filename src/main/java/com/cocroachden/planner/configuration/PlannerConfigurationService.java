package com.cocroachden.planner.configuration;

import com.cocroachden.planner.lib.WorkerId;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@BrowserCallable
@AnonymousAllowed
@AllArgsConstructor
public class PlannerConfigurationService {
  private PlannerConfigurationRepository repository;

  public @Nonnull PlannerConfigurationResponse getLatestConfiguration() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .max(Comparator.comparing(PlannerConfigurationRecord::getCreatedAt))
        .map(PlannerConfigurationResponse::from)
        .orElseThrow(() -> new RuntimeException("No planner configuration was found"));
  }

  public @Nonnull PlannerConfigurationResponse getConfiguration(UUID uuid) {
    return PlannerConfigurationResponse.from(repository.getById(uuid));
  }

  @Nonnull
  public List<@Nonnull PlannerConfigurationMetaData> getMetaData() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(c -> new PlannerConfigurationMetaData(
            c.getName(),
            c.getId(),
            c.getStartDate(),
            c.getEndDate()
        )).toList();
  }

  public PlannerConfigurationResponse upsert(PlannerConfigurationResponse record) {
    return PlannerConfigurationResponse.from(repository.save(PlannerConfigurationRecord.from(record)));
  }

  public PlannerConfigurationResponse saveAsNew(PlannerConfigurationResponse record) {
    return PlannerConfigurationResponse.from(repository.save(PlannerConfigurationRecord.from(record)));
  }

  public List<PlannerConfigurationResponse> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .map(PlannerConfigurationResponse::from)
        .toList();
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  public static class PlannerConfigurationResponse {
    public static PlannerConfigurationResponse from(PlannerConfigurationRecord record) {
      return new PlannerConfigurationResponse(
          record.getId(),
          record.getName(),
          record.getCreatedAt(),
          record.getLastUpdated(),
          record.getStartDate(),
          record.getEndDate(),
          record.getWorkers(),
          record.getConstraintRequestInstances()
      );
    }
    @Nonnull
    private UUID id;
    @Nonnull
    private String name;
    @Nonnull
    private Instant createdAt;
    @Nonnull
    private Instant lastUpdated;
    @Nonnull
    private LocalDate startDate;
    @Nonnull
    private LocalDate endDate;
    @Nonnull
    private List<@Nonnull WorkerId> workers;
    @Nonnull
    private List<@Nonnull UUID> constraintRequestInstances;
  }
}
