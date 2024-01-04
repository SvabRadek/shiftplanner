package com.cocroachden.planner;

import com.cocroachden.planner.configuration.PlannerConfigurationRecord;
import com.cocroachden.planner.configuration.PlannerConfigurationRepository;
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
    var plannerConfigurationRepository = context.getBean(PlannerConfigurationRepository.class);
    plannerConfigurationRepository.save(
        new PlannerConfigurationRecord(
            0L,
            LocalDate.now(),
            LocalDate.now().plusDays(30),
            List.of(),
            List.of()
        )
    );
    log.info("Repositories has been initialized!");
  }
}
