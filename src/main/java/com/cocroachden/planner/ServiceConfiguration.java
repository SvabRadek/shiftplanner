package com.cocroachden.planner;

import com.cocroachden.planner.solver.constraints.GenericConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.consecutiveworkingdays.ConsecutiveWorkingDaysConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftfollowuprestriction.ShiftFollowUpConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftpattern.ShiftPatternConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftperday.OneShiftPerDayConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.shiftperschedule.ShiftsPerScheduleConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.teamassignment.TeamAssignmentConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.tripleshift.TripleShiftConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.employeeshiftrequest.EmployeeShiftRequestConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.employeespershift.EmployeesPerShiftConstraintApplier;
import com.cocroachden.planner.solver.constraints.specific.weekends.WeekendRequestsApplier;
import com.cocroachden.planner.solver.service.SolverService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ServiceConfiguration {
  @Bean
  public GenericConstraintApplier genericConstraintApplier() {
    return new GenericConstraintApplier(
        new OneShiftPerDayConstraintApplier(),
        new ShiftsPerScheduleConstraintApplier(),
        new EmployeeShiftRequestConstraintApplier(),
        new EmployeesPerShiftConstraintApplier(),
        new ShiftFollowUpConstraintApplier(),
        new ConsecutiveWorkingDaysConstraintApplier(),
        new ShiftPatternConstraintApplier(),
        new TripleShiftConstraintApplier(),
        new TeamAssignmentConstraintApplier(),
        new WeekendRequestsApplier()
    );
  }

  @Bean
  public SolverService alternativeSolver(GenericConstraintApplier constraintApplier) {
    return new SolverService(constraintApplier);
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
