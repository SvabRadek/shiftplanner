package com.cocroachden.planner.employee.fixture;

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
        return List.of(
                new SaveEmployeeCommand(EmployeeId.random(), "Martina", "Kravariková"),
                new SaveEmployeeCommand(EmployeeId.random(), "Vendula", "Zajícová"),
                new SaveEmployeeCommand(EmployeeId.random(), "Alena", "Janáková"),
                new SaveEmployeeCommand(EmployeeId.random(), "Simona", "Holmanová"),
                new SaveEmployeeCommand(EmployeeId.random(), "Jana", "Zelenková"),
                new SaveEmployeeCommand(EmployeeId.random(), "Jolana", "Pálffyová"),
                new SaveEmployeeCommand(EmployeeId.random(), "Nicola", "Halbichová"),
                new SaveEmployeeCommand(EmployeeId.random(), "Jana", "Kesslerová"),
                new SaveEmployeeCommand(EmployeeId.random(), "Eva", "Dudek Premauer"),
                new SaveEmployeeCommand(EmployeeId.random(), "Aneta", "Dubská"),
                new SaveEmployeeCommand(EmployeeId.random(), "Jindra", "Labounková"),
                new SaveEmployeeCommand(EmployeeId.random(), "Dana", "Zachová"),
                new SaveEmployeeCommand(EmployeeId.random(), "Iva", "Najmanová"),
                new SaveEmployeeCommand(EmployeeId.random(), "Barbora", "Řeháková"),
                new SaveEmployeeCommand(EmployeeId.random(), "Karolína", "Vavrušková"),
                new SaveEmployeeCommand(EmployeeId.random(), "Zuzana", "Kučerová"),
                new SaveEmployeeCommand(EmployeeId.random(), "Natálie", "Vejvodová"),
                new SaveEmployeeCommand(EmployeeId.random(), "Karolína", "Hromířová")
        );
    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
