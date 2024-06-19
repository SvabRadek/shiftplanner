package com.cocroachden.planner;

import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class StartupService {

  @EventListener
  public void on(ApplicationStartedEvent event) {
    var context = event.getApplicationContext();
    var configRepo = context.getBean(SolverConfigurationRepository.class);
    if (isThereAnyConfig(configRepo)) {
      return;
    }
    var employeeRepo = context.getBean(EmployeeRepository.class);
    var configRecord = new SolverConfigurationRecord();
    configRecord.setStartDate(LocalDate.now());
    configRecord.setEndDate(LocalDate.now().plusDays(30));
    configRecord.setName("Priklad konfigurace");
    List<EmployeeRecord> employees = DefaultSolverConfiguration.employees().stream()
        .peek(e -> e.addConfiguration(configRecord))
        .toList();
    configRecord.setEmployees(employees);
    configRecord.setConstraintRequestRecords(
        DefaultSolverConfiguration.constraintRequests().stream()
            .peek(c -> c.setParent(configRecord))
            .toList()
    );
    configRepo.save(configRecord);
    employees.stream()
        .filter(e -> !employeeRepo.existsById(e.getId()))
        .forEach(employeeRepo::save);
  }

  private static boolean isThereAnyConfig(SolverConfigurationRepository configRepo) {
    return configRepo.findAll().iterator().hasNext();
  }

}
