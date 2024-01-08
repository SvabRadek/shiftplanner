package com.cocroachden.planner;

import com.cocroachden.planner.configuration.ConstraintRequestRecord;
import com.cocroachden.planner.configuration.ConstraintRequestService;
import com.cocroachden.planner.configuration.PlannerConfigurationRecord;
import com.cocroachden.planner.configuration.PlannerConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
public class StartupService {
  @EventListener
  public void on(ApplicationStartedEvent event) {
    var context = event.getApplicationContext();
    var configService = context.getBean(PlannerConfigurationService.class);
    var requestService = context.getBean(ConstraintRequestService.class);
    if (configService.findAll().isEmpty()) {
      var configId = UUID.randomUUID();
      var requestIds = Example.constraintRequests().stream()
          .map(constraint -> new ConstraintRequestRecord(constraint, configId))
          .map(requestService::upsert)
          .map(ConstraintRequestRecord::getId)
          .toList();
      configService.upsert(
          new PlannerConfigurationRecord(
              configId,
              "Priklad konfigurace",
              Instant.now(),
              Instant.now(),
              LocalDate.now(),
              LocalDate.now().plusDays(30),
              Example.workers(),
              requestIds
          )
      );
    }
    log.info("Repositories has been initialized!");
  }
}
