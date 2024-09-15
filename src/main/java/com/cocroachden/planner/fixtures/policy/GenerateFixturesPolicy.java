package com.cocroachden.planner.fixtures.policy;

import com.cocroachden.planner.fixtures.command.generatefixtures.GenerateFixturesCommand;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class GenerateFixturesPolicy {
    @EventListener(ApplicationReadyEvent.class)
    public GenerateFixturesCommand on() {
        return new GenerateFixturesCommand();
    }
}
