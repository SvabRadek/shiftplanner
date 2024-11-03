package com.cocroachden.planner.fixtures.policy;

import com.cocroachden.planner.fixtures.command.generatefixtures.GenerateFixturesCommand;
import com.cocroachden.planner.fixtures.configuration.FixtureModuleConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class GenerateFixturesPolicy {

    private FixtureModuleConfiguration moduleConfiguration;

    @EventListener(ApplicationReadyEvent.class)
    public GenerateFixturesCommand on() {
        var enabled = moduleConfiguration.getEnabled();
        var allowed = moduleConfiguration.getFixturesToApply();
        if (!enabled) {
            return null;
        }
        return new GenerateFixturesCommand(Arrays.stream(allowed).toList());
    }
}
