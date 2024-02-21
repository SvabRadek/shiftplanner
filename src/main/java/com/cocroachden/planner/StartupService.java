package com.cocroachden.planner;

import com.cocroachden.planner.constraint.repository.ConstraintRequestRecord;
import com.cocroachden.planner.constraint.service.ConstraintRequestService;
import com.cocroachden.planner.lib.StupidDate;
import com.cocroachden.planner.plannerconfiguration.ConfigurationRequestLinkDTO;
import com.cocroachden.planner.plannerconfiguration.PlannerConfigurationDTO;
import com.cocroachden.planner.plannerconfiguration.service.PlannerConfigurationService;
import com.cocroachden.planner.employee.EmployeeRecord;
import com.cocroachden.planner.employee.EmployeeRepository;
import jakarta.transaction.Transactional;
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
          .map(requestService::saveAsNew)
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
              StupidDate.fromDate(LocalDate.now()),
              StupidDate.fromDate(LocalDate.now().plusDays(30)),
              Example.workers(),
              requestLinks
          )
      );
    }
  }

  private void createEmployeeRecords(ApplicationContext context) {
    var employeeRepo = context.getBean(EmployeeRepository.class);
    var employees = List.of(
        new EmployeeRecord(
            "0",
            "Martina",
            "Kravariková"
        ),
        new EmployeeRecord(
            "1",
            "Vendula",
            "Zajícová"
        ),
        new EmployeeRecord(
            "2",
            "Alena",
            "Janáková"
        ),
        new EmployeeRecord(
            "3",
            "Simona",
            "Holmanová"
        ),
        new EmployeeRecord(
            "4",
            "Jana",
            "Zelenková"
        ),
        new EmployeeRecord(
            "5",
            "Jolana",
            "Pálffyová"
        ),
        new EmployeeRecord(
            "6",
            "Nicola",
            "Halbichová"
        ),
        new EmployeeRecord(
            "7",
            "Jana",
            "Kesslerová"
        ),
        new EmployeeRecord(
            "8",
            "Eva",
            "Dudek Premauer"
        ),
        new EmployeeRecord(
            "9",
            "Aneta",
            "Dubská"
        ),
        new EmployeeRecord(
            "10",
            "Jindra",
            "Labounková"
        ),
        new EmployeeRecord(
            "11",
            "Dana",
            "Zachová"
        ),
        new EmployeeRecord(
            "12",
            "Iva",
            "Najmanová"
        ),
        new EmployeeRecord(
            "13",
            "Barbora",
            "Řeháková"
        ),
        new EmployeeRecord(
            "14",
            "Karolína",
            "Vavrušková"
        ),
        new EmployeeRecord(
            "15",
            "Zuzana",
            "Kučerová"
        ),
        new EmployeeRecord(
            "16",
            "Natálie",
            "Vejvodová"
        ),
        new EmployeeRecord(
            "17",
            "Karolína",
            "Hromířová"
        )
    );
    employeeRepo.saveAll(employees);
  }
}
