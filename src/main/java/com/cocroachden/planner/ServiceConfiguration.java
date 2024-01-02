package com.cocroachden.planner;

import com.cocroachden.planner.solver.constraints.GenericConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.ConsecutiveWorkingDaysConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.ShiftFollowUpConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.ShiftPatternPositiveConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.OneShiftPerDayConstraint;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.ShiftsPerScheduleConstraint;
import com.cocroachden.planner.solver.constraints.specific.workershiftrequest.WorkerShiftRequestConstraint;
import com.cocroachden.planner.solver.constraints.specific.workerspershift.WorkersPerShiftConstraint;
import com.cocroachden.planner.solver.solver.ScheduleSolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ServiceConfiguration {
  @Bean
  public GenericConstraintApplier genericConstraintApplier() {
    return new GenericConstraintApplier(
        new OneShiftPerDayConstraint(),
        new ShiftsPerScheduleConstraint(),
        new WorkerShiftRequestConstraint(),
        new WorkersPerShiftConstraint(),
        new ShiftFollowUpConstraint(),
        new ConsecutiveWorkingDaysConstraint(),
        new ShiftPatternPositiveConstraint()
    );
  }

  @Bean
  public ScheduleSolver alternativeSolver(GenericConstraintApplier constraintApplier) {
    return new ScheduleSolver(constraintApplier);
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
