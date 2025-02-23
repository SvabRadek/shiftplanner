package com.cocroachden.planner.employee.fixtures;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.employee.command.saveemployee.SaveEmployeeCommand;
import com.cocroachden.planner.fixtures.SpecificFixtureGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeFixtureGenerator implements SpecificFixtureGenerator {

    public static final String NAME = "employee_fixtures";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<Command> generateCommands() {
        return EmployeeFixturesData.fixtureEmployees().stream()
                .map(employeeDTO -> new SaveEmployeeCommand(
                        employeeDTO.getId(),
                        employeeDTO.getFirstName(),
                        employeeDTO.getLastName(),
                        "user@planning.com"
                )).map(Command.class::cast)
                .toList();
    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
