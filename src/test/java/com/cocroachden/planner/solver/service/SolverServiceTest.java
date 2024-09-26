package com.cocroachden.planner.solver.service;

import com.cocroachden.planner.AbstractMessagingTest;
import com.cocroachden.planner.solver.SolverSolutionDTO;
import com.cocroachden.planner.solver.SolverSubscriptionId;
import com.cocroachden.planner.solver.command.solveconfiguration.SolutionHasBeenFound;
import com.cocroachden.planner.solver.command.solveconfiguration.SolverHasBeenStarted;
import com.cocroachden.planner.solver.command.solveconfiguration.StartSolverCommand;
import com.cocroachden.planner.solver.command.stopsolver.SolverHasBeenStopped;
import com.cocroachden.planner.solver.command.stopsolver.StopSolverCommand;
import com.cocroachden.planner.solver.query.SolverServiceQuery;
import com.cocroachden.planner.solver.solver.SolverFactory;
import com.cocroachden.planner.solver.service.testimplementation.TestSolver;
import com.cocroachden.planner.solver.service.testimplementation.TestSolverFactory;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Import(SolverServiceTest.SolverServiceTestConfiguration.class)
class SolverServiceTest extends AbstractMessagingTest {

    @Autowired
    private SolverConfigurationRepository configurationRepository;

    @Autowired
    private TestSolverFactory testSolverFactory;

    @Autowired
    private SolverServiceQuery solverServiceQuery;

    @BeforeEach
    public void setup() {
        configurationRepository.deleteAll();
    }

    @Test
    public void itCanStartSolvingProblems() {
        testSolverFactory.setSolver(
                new TestSolver(
                        (configuration, callback, solverOptions) -> {
                            callback.accept(new SolverSolutionDTO());
                        }
                )
        );
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
                10,
                "irrelevant"
        );
        this.whenCommandHasBeenSent(command);
        this.thenExactlyOneEventHasBeenDispatched(SolverHasBeenStarted.class);
        this.thenSomeTimeHasPassed(10);
        this.thenAtLeastOneEventHasBeenDispatched(SolutionHasBeenFound.class);
        this.thenExactlyOneEventHasBeenDispatched(SolverHasBeenStopped.class);
        Assertions.assertThat(solverServiceQuery.findSolverTask(subscriptionId)).isEmpty();
    }

    @Test
    public void itStopsSolverWhenSolverInNewThreadThrows() {
        testSolverFactory.setSolver(new TestSolver(
                (configuration, callback, solverOptions) -> {
                    throw new IllegalStateException("Test exception");
                }
        ));
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
        var startCommand = new StartSolverCommand(
                configurationId,
                subscriptionId,
                10,
                "irrelevant"
        );
        this.whenCommandHasBeenSent(startCommand);
        this.thenExactlyOneEventHasBeenDispatched(SolverHasBeenStarted.class);
        this.thenSomeTimeHasPassed(10);
        this.thenExactlyOneEventHasBeenDispatched(SolverHasBeenStopped.class);
        Assertions.assertThat(solverServiceQuery.findSolverTask(subscriptionId)).isEmpty();
    }

    @Test
    public void itCanCancelRunningSolver() {
        var shouldStop = new AtomicBoolean(false);
        testSolverFactory.setSolver(
                new TestSolver()
                        .setTestCallback((configuration, callback, solverOptions) -> {
                            while (!shouldStop.get()) {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }).setTestStopCallback(() -> shouldStop.set(true))
        );
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
        var startCommand = new StartSolverCommand(
                configurationId,
                subscriptionId,
                10,
                "irrelevant"
        );
        this.givenCommandHasBeenSent(startCommand);
        Assertions.assertThat(solverServiceQuery.findSolverTask(subscriptionId)).isPresent();
        var cancelCommand = new StopSolverCommand(subscriptionId, "irrelevant");
        this.whenCommandHasBeenSent(cancelCommand);
        this.thenSomeTimeHasPassed(50);
        this.thenExactlyOneEventHasBeenDispatched(SolverHasBeenStopped.class);
        Assertions.assertThat(solverServiceQuery.findSolverTask(subscriptionId)).isEmpty();
    }

    @TestConfiguration
    public static class SolverServiceTestConfiguration {
        @Bean
        @Primary
        public SolverFactory testSolverFactory() {
            return new TestSolverFactory(
                    new TestSolver(
                            (configuration, callback, solverOptions) -> callback.accept(new SolverSolutionDTO())
                    )
            );
        }
    }

}