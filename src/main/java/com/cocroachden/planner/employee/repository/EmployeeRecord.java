package com.cocroachden.planner.employee.repository;

import dev.hilla.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Employee")
@Table(name = "employees")
public final class EmployeeRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  private String firstName;
  private String lastName;

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EmployeeRecord other) {
      return other.id.equals(this.id);
    }
    return false;
  }

  public EmployeeRecord(@Nonnull Long id, @Nonnull String firstName, @Nonnull String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}