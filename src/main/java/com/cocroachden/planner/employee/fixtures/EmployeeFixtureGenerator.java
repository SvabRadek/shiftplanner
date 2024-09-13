package com.cocroachden.planner.employee.fixtures;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.command.saveemployee.SaveEmployeeCommand;
import com.cocroachden.planner.fixtures.SpecificFixtureGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeFixtureGenerator implements SpecificFixtureGenerator {
    @Override
    public List<Command> generateCommands() {
        return EmployeeFixturesData.fixtureEmployees().stream()
                .map(employeeDTO -> new SaveEmployeeCommand(
                        new EmployeeId(employeeDTO.getId()),
                        employeeDTO.getFirstName(),
                        employeeDTO.getLastName()
                )).map(Command.class::cast)
                .toList();
    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
