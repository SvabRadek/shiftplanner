package com.cocroachden.planner.employee;

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
  private Long id;
  @NotEmpty(message = "Jmeno nemuze by prazdne")
  private String firstName;
  @NotEmpty(message = "Prijmeni nemuze by prazdne")
  private String lastName;

  public EmployeeRecord(
      String firstName,
      String lastName
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}