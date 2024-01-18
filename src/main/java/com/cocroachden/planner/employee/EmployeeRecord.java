package com.cocroachden.planner.employee;

import dev.hilla.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "employees")
public final class EmployeeRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Nonnull
  private Long id;
  @Nonnull
  @NotEmpty(message = "Pracovnik musi mit id")
  private String workerId;
  @NotEmpty(message = "Jmeno nemuze by prazdne")
  @Nonnull
  private String firstName;
  @NotEmpty(message = "Prijmeni nemuze by prazdne")
  @Nonnull
  private String lastName;

  public EmployeeRecord(
      String workerId,
      String firstName,
      String lastName
  ) {
    this.workerId = workerId;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}