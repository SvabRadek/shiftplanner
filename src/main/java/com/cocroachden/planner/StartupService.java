package com.cocroachden.planner;

import com.cocroachden.planner.configuration.ConstraintRequestRecord;
import com.cocroachden.planner.configuration.PlannerConfigurationRecord;
import com.cocroachden.planner.configuration.PlannerConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import java.time.Instant;
import java.time.LocalDate;

@Slf4j
public class StartupService {
  @EventListener
  public void on(ApplicationStartedEvent event) {
    var context = event.getApplicationContext();
    var configService = context.getBean(PlannerConfigurationService.class);
    if (configService.findAll().isEmpty()) {
      configService.save(
          new PlannerConfigurationRecord(
              0L,
              Instant.now(),
              Instant.now(),
              LocalDate.now(),
              LocalDate.now().plusDays(30),
              Example.workers(),
              Example.constraintRequests()
                  .stream()
                  .map(ConstraintRequestRecord::new)
                  .toList()
          )
      );
    }
    log.info("Repositories has been initialized!");
  }
}
