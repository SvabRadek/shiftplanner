package com.cocroachden.planner.solver.service;

import com.cocroachden.AbstractMessagingTest;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.command.solveconfiguration.SolutionHasBeenFound;
import com.cocroachden.planner.solver.command.solveconfiguration.StartSolverCommand;
import com.cocroachden.planner.solver.service.testimplementation.TestSolver;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.util.List;

@Import(SolverServiceTest.SolverServiceTestConfiguration.class)
class SolverServiceTest extends AbstractMessagingTest {

    @Test
    public void itCanStartSolvingProblems() {
        var subscriptionId = SolverSubscriptionId.random();
        SolverConfigurationId configurationId = SolverConfigurationId.from("irrelevant");
        var configCommand = new SaveSolverConfigurationCommand(
                configurationId,
                "irrelevant",
                "irrelevant",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(),
                List.of()
        );
        this.givenCommandHasBeenSent(configCommand);
        var command = new StartSolverCommand(
                configurationId,
                subscriptionId,
                10
        );
        this.whenCommandHasBeenSent(command);
        this.thenExactlyOneEventHasBeenDispatched(SolutionHasBeenFound.class);
    }

    @TestConfiguration
    public static class SolverServiceTestConfiguration {
        @Bean
        @Primary
        public Solver testSolver() {
            return new TestSolver();
        }
    }

}