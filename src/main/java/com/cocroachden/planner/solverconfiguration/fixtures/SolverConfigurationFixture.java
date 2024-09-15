package com.cocroachden.planner.solverconfiguration.fixtures;

import com.cocroachden.planner.common.messaging.Command;
import com.cocroachden.planner.constraint.*;
import com.cocroachden.planner.employee.EmployeeDTO;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.fixtures.EmployeeFixturesData;
import com.cocroachden.planner.fixtures.SpecificFixtureGenerator;
import com.cocroachden.planner.solver.api.WorkShifts;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentDTO;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.command.saveconfiguration.SaveSolverConfigurationCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class SolverConfigurationFixture implements SpecificFixtureGenerator {

    private final static SolverConfigurationId DEFAULT_ID = new SolverConfigurationId("4e8c8725-3fc8-4895-bf4a-8d1af153c7c5");

    @Override
    public List<Command> generateCommands() {
        var fixtureEmployees = EmployeeFixturesData.fixtureEmployees();
        var constraints = new ArrayList<>(
                fixtureEmployees.stream()
                        .map(EmployeeDTO::getId)
                        .map(EmployeeId::new)
                        .map(this::generateFixtureConstrainsForEmployee)
                        .flatMap(List::stream)
                        .toList()
        );
        constraints.addAll(this.generateConstraintsForSchedule());
        var assignments = IntStream.range(0, fixtureEmployees.size())
                .mapToObj(i -> {
                    var e = fixtureEmployees.get(i);
                    return new EmployeeAssignmentDTO(e.getId(), i, 1);
                }).toList();

        return List.of(
                new SaveSolverConfigurationCommand(
                        DEFAULT_ID,
                        "Priklad konfigurace",
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        assignments,
                        constraints
                )
        );
    }

    @Override
    public Integer getOrder() {
        return 1;
    }

    private List<ConstraintDTO> generateConstraintsForSchedule() {
        return List.of(
                new EmployeesPerShiftConstraintDTO(
                        ConstraintId.random().getId(),
                        WorkShifts.NIGHT,
                        2,
                        2,
                        1,
                        2,
                        1,
                        2
                ),
                new EmployeesPerShiftConstraintDTO(
                        ConstraintId.random().getId(),
                        WorkShifts.DAY,
                        3,
                        3,
                        1,
                        3,
                        1,
                        3
                )
        );
    }

    private List<ConstraintDTO> generateFixtureConstrainsForEmployee(EmployeeId employeeId) {
        return List.of(
                new ShiftsPerScheduleConstraintDTO(
                        ConstraintId.random().getId(),
                        employeeId.getId(),
                        WorkShifts.WORKING_SHIFTS,
                        13,
                        14,
                        1,
                        14,
                        15,
                        1
                ),
                new TripleShiftConstraintDTO(
                        ConstraintId.random().getId(),
                        employeeId.getId(),
                        50,
                        true
                ),
                new ShiftFollowupRestrictionConstraintDTO(
                        ConstraintId.random().getId(),
                        employeeId.getId(),
                        WorkShifts.NIGHT,
                        WorkShifts.DAY,
                        0
                ),
                new ConsecutiveWorkingDaysConstraintDTO(
                        ConstraintId.random().getId(),
                        employeeId.getId(),
                        WorkShifts.WORKING_SHIFTS,
                        0,
                        2,
                        1,
                        2,
                        1,
                        3
                )
        );
    }
}
