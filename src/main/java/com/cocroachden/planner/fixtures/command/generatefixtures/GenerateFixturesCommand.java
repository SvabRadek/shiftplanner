package com.cocroachden.planner.fixtures.command.generatefixtures;

import com.cocroachden.planner.common.messaging.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GenerateFixturesCommand implements Command {
    private List<String> fixtureGeneratorsToApply = List.of();
}
