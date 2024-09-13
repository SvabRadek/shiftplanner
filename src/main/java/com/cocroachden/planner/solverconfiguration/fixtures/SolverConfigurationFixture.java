package com.cocroachden.planner.solverconfiguration.fixtures;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.fixtures.EmployeeFixturesData;
import com.cocroachden.planner.fixtures.SpecificFixtureGenerator;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class SolverConfigurationFixture implements SpecificFixtureGenerator {

    private final static SolverConfigurationId DEFAULT_ID = new SolverConfigurationId("4e8c8725-3fc8-4895-bf4a-8d1af153c7c5");

    @Override
    public List<Command> generateCommands() {
        var fixtureEmployees = EmployeeFixturesData.fixtureEmployees();
        return Stream.of(
                        new SaveSolverConfigurationCommand(
                                DEFAULT_ID,
                                "Priklad konfigurace",
                                LocalDate.now(),
                                LocalDate.now().plusDays(30),
                                IntStream.range(0, fixtureEmployees.size())
                                        .mapToObj(i -> {
                                            var e = fixtureEmployees.get(i);
                                            return new EmployeeAssignmentDTO(EmployeeId.from(e.getId()), i, 1);
                                        }).toList(),
                                List.of()
                        )
                ).map(Command.class::cast)
                .toList();
    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
