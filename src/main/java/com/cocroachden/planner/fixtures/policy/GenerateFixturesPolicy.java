package com.cocroachden.planner.fixtures.policy;

import com.cocroachden.planner.fixtures.command.generatefixtures.GenerateFixturesCommand;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class GenerateFixturesPolicy {
    @EventListener
    public GenerateFixturesCommand on(ContextRefreshedEvent event) {
        return new GenerateFixturesCommand();
    }
}
