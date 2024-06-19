package com.cocroachden.planner.employee.repository;

import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;
import dev.hilla.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Employee")
@Table(name = "employees")
public final class EmployeeRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Nonnull
  private Long id;
  @NotEmpty(message = "Jmeno nemuze by prazdne")
  @Nonnull
  private String firstName;
  @NotEmpty(message = "Prijmeni nemuze by prazdne")
  @Nonnull
  private String lastName;

  @ManyToMany(mappedBy = "employees")
  private List<SolverConfigurationRecord> configurations = new ArrayList<>();

  public EmployeeRecord(@Nonnull Long id, @Nonnull String firstName, @Nonnull String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void addConfiguration(SolverConfigurationRecord configuration) {
    this.configurations.add(configuration);
  }
}