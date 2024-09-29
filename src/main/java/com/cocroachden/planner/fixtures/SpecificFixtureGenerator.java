package com.cocroachden.planner.fixtures;

import com.cocroachden.planner.common.messaging.Command;

import java.util.List;

public interface SpecificFixtureGenerator {
    String getName();
    List<Command> generateCommands();
    Integer getOrder();
}
