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
public class GenericFixturesGenerator {

    private final List<SpecificFixtureGenerator> generators;
    private final ApplicationEventPublisher publisher;

    @EventListener
    public FixturesHasBeenGenerated handle(GenerateFixturesCommand c) {
        generators.stream()
                .sorted(Comparator.comparing(SpecificFixtureGenerator::getOrder))
                .map(SpecificFixtureGenerator::generateCommands)
                .flatMap(List::stream)
                .forEach(command -> {
                    try {
                        publisher.publishEvent(command);
                    } catch (Exception e) {
                        log.warn("Exception was thrown by command {} due to {}", command.getClass().getSimpleName(), e.getMessage());
                    }
                });
        return new FixturesHasBeenGenerated();
    }

}
