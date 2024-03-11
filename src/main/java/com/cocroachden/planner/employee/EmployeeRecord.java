package com.cocroachden.planner.employee;

import dev.hilla.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
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

  public EmployeeRecord(@Nonnull Long id, @Nonnull String firstName, @Nonnull String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}