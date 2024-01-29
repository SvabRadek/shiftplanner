package com.cocroachden.planner;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.service.ConstraintRequestService;
import com.cocroachden.planner.plannerconfiguration.ConfigurationRequestLinkDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.plannerconfiguration.service.PlannerConfigurationService;
import com.cocroachden.planner.employee.EmployeeRecord;
import com.cocroachden.planner.employee.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
    var configService = context.getBean(PlannerConfigurationService.class);
    var requestService = context.getBean(ConstraintRequestService.class);
    if (configService.findAll().isEmpty()) {
      var configId = UUID.randomUUID();
      var requestLinks = Example.constraintRequests().stream()
          .map(ConstraintRequestRecord::new)
          .map(requestService::upsert)
          .map(record -> new ConfigurationRequestLinkDTO(
              record.getType(),
              record.getId()
          )).toList();
      configService.save(
          new PlannerConfigurationDTO(
              configId,
              "Priklad konfigurace",
              Instant.now(),
              Instant.now(),
              LocalDate.now(),
              LocalDate.now().plusDays(30),
              Example.workers(),
              requestLinks
          )
      );
    }
  }

  private void createEmployeeRecords(ApplicationContext context) {
    List<String> firstNames = Arrays.asList(
        "John", "Jane", "Alice", "Bob", "Charlie",
        "David", "Eva", "Frank", "Grace", "Henry",
        "Ivy", "Jack", "Kate", "Leo", "Mia"
    );
    List<String> lastNames = Arrays.asList(
        "Anderson", "Brown", "Clark", "Davis", "Evans",
        "Fisher", "Garcia", "Hill", "Irwin", "Jones",
        "King", "Lee", "Miller", "Nelson", "Owens"
    );
    var employeeRepo = context.getBean(EmployeeRepository.class);
    Example.workers().stream()
        .map(id -> new EmployeeRecord(
            id.getWorkerId(),
            firstNames.get(new Random().nextInt(firstNames.size())),
            lastNames.get(new Random().nextInt(lastNames.size()))
        )).forEach(employeeRepo::save);
  }
}
