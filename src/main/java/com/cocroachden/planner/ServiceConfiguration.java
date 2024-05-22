package com.cocroachden.planner;

import com.cocroachden.planner.constraint.api.ConstraintRequestDTO;
import com.cocroachden.planner.lib.ConstraintRequestDTODeserializer;
import com.cocroachden.planner.solver.constraints.GenericConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.ConsecutiveWorkingDaysConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.ShiftFollowUpConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.ShiftPatternConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.OneShiftPerDayConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.ShiftsPerScheduleConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.WorkerShiftRequestConstraintApplier;
import com.cocroachden.planner.solver.constraints.validator.specific.SpecificShiftRequestValidator;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.WorkersPerShiftConstraintApplier;
import com.cocroachden.planner.solver.constraints.validator.ConstraintValidator;
import com.cocroachden.planner.solver.solver.ScheduleSolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;

@Configuration
public class ServiceConfiguration {
  @Bean
  public GenericConstraintApplier genericConstraintApplier() {
    return new GenericConstraintApplier(
        new OneShiftPerDayConstraintApplier(),
        new ShiftsPerScheduleConstraintApplier(),
        new WorkerShiftRequestConstraintApplier(),
        new WorkersPerShiftConstraintApplier(),
        new ShiftFollowUpConstraintApplier(),
        new ConsecutiveWorkingDaysConstraintApplier(),
        new ShiftPatternConstraintApplier()
    );
  }

  @Bean
  public ConstraintValidator constraintValidator() {
    return new ConstraintValidator(
        List.of(
            new SpecificShiftRequestValidator()
        )
    );
  }

  @Bean
  public ScheduleSolver alternativeSolver(GenericConstraintApplier constraintApplier) {
    return new ScheduleSolver(constraintApplier);
  }

  @Bean
  public StartupService startupService() {
    return new StartupService();
  }

  @Bean
  public Executor taskExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(2);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("CPSolver-");
    executor.initialize();
    return executor;
  }
}
