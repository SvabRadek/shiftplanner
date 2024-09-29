package com.cocroachden.planner.fixtures.service;

import com.cocroachden.planner.fixtures.SpecificFixtureGenerator;
import com.cocroachden.planner.fixtures.command.generatefixtures.FixturesHasBeenGenerated;
import com.cocroachden.planner.fixtures.command.generatefixtures.GenerateFixturesCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FixturesService {

    private final List<SpecificFixtureGenerator> generators;
    private final ApplicationEventPublisher publisher;

    @EventListener
    public FixturesHasBeenGenerated handle(GenerateFixturesCommand command) {
        log.debug("Handling GenerateFixturesCommand...");
        var allowedGenerators = command.getFixtureGeneratorsToApply();
        generators.stream()
                .filter(generator -> {
                    if (allowedGenerators.isEmpty()) {
                        return true;
                    }
                    return allowedGenerators.contains(generator.getName());
                })
                .sorted(Comparator.comparing(SpecificFixtureGenerator::getOrder))
                .forEach(generator -> {
                    log.debug("Applying fixture generator [{}]", generator.getName());
                    generator.generateCommands().forEach(fixtureCommand -> {
                        try {
                            publisher.publishEvent(fixtureCommand);
                        } catch (Exception e) {
                            log.warn("Exception was thrown by command {} due to {}", fixtureCommand.getClass().getSimpleName(), e.getMessage());
                        }
                    });
                });
        return new FixturesHasBeenGenerated();
    }

}
