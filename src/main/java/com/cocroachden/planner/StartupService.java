package com.cocroachden.planner;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.employee.repository.EmployeeRecord;
import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solver.repository.SolverConfigurationRecord;
import com.cocroachden.planner.solver.repository.SolverConfigurationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
public class StartupService {

  @EventListener
  public void on(ApplicationStartedEvent event) {
    var context = event.getApplicationContext();
    this.createEmployeeRecords(context);
    this.createConfigurations(context);
    log.info("Repositories has been initialized!");
  }

  private void createConfigurations(ApplicationContext context) {
    var configRepo = context.getBean(SolverConfigurationRepository.class);
    if (!configRepo.findAll().iterator().hasNext()) {
      var configRecord = new SolverConfigurationRecord();
      configRecord.setStartDate(LocalDate.now());
      configRecord.setEndDate(LocalDate.now().plusDays(30));
      configRecord.setName("Priklad konfigurace");
      configRecord.setWorkers(DefaultSolverConfiguration.workers());
      configRecord.setConstraintRequestRecords(
          DefaultSolverConfiguration.constraintRequests().stream()
              .map(c -> {
                var record = new ConstraintRequestRecord();
                record.setType(c.getType());
                record.setParent(configRecord);
                record.setRequest(c);
                return record;
              }).toList()
      );
      configRepo.save(configRecord);
    }
  }

  private void createEmployeeRecords(ApplicationContext context) {
    var employeeRepo = context.getBean(EmployeeRepository.class);
    var employees = List.of(
        new EmployeeRecord(1L, "Martina", "Kravariková"),
        new EmployeeRecord(2L, "Vendula", "Zajícová"),
        new EmployeeRecord(3L, "Alena", "Janáková"),
        new EmployeeRecord(4L, "Simona", "Holmanová"),
        new EmployeeRecord(5L, "Jana", "Zelenková"),
        new EmployeeRecord(6L, "Jolana", "Pálffyová"),
        new EmployeeRecord(7L, "Nicola", "Halbichová"),
        new EmployeeRecord(8L, "Jana", "Kesslerová"),
        new EmployeeRecord(9L, "Eva", "Dudek Premauer"),
        new EmployeeRecord(10L, "Aneta", "Dubská"),
        new EmployeeRecord(11L, "Jindra", "Labounková"),
        new EmployeeRecord(12L, "Dana", "Zachová"),
        new EmployeeRecord(13L, "Iva", "Najmanová"),
        new EmployeeRecord(14L, "Barbora", "Řeháková"),
        new EmployeeRecord(15L, "Karolína", "Vavrušková"),
        new EmployeeRecord(16L, "Zuzana", "Kučerová"),
        new EmployeeRecord(17L, "Natálie", "Vejvodová"),
        new EmployeeRecord(18L, "Karolína", "Hromířová")
    );
    employees.stream()
        .filter(e -> !employeeRepo.existsById(e.getId()))
        .forEach(employeeRepo::save);
  }
}
