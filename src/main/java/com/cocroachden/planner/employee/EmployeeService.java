package com.cocroachden.planner.employee;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import dev.hilla.Nonnull;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class EmployeeService {

  @Nonnull
  public List<@Nonnull EmployeeRecord> getEmployees() {
    return List.of(
        new EmployeeRecord("fart", "Petr", "Spalnicka"),
        new EmployeeRecord("mud", "Jana", "Vredova")
    );
  }
  public record EmployeeRecord(
      String id,
      String firstName,
      String lastName
  ) {}

}


