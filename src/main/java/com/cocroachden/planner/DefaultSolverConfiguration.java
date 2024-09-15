package com.cocroachden.planner;


import com.cocroachden.planner.constraint.repository.ConstraintRecord;
import com.cocroachden.planner.employee.EmployeeId;
import com.cocroachden.planner.employee.repository.EmployeeRecord;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DefaultSolverConfiguration {
    private static final LocalDate FIRST_DAY = LocalDate.of(2023, 11, 1);
    private static final LocalDate LAST_DAY = LocalDate.of(2023, 11, 30);

    public static List<ConstraintRecord> constraintRequests() {
//        var allRequests = new ArrayList<ConstraintRequest>();
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("1L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("1L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("1L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("2L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("2L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("2L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("3L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("3L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("3L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("4L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("4L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("4L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("5L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("5L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("5L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("6L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("6L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("6L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("7L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("7L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("7L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("8L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("8L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("8L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("9L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("9L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("9L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("10L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("10L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("10L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("11L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("11L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("11L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("12L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("12L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("12L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("13L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("13L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("13L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("14L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("14L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("14L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("15L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("15L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("15L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("16L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("16L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("16L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("17L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("17L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("17L"), true, 50));
//        allRequests.add(new ShiftsPerScheduleRequest(new EmployeeId("18L"), 13, 14, 1, 14, 1, 15));
//        allRequests.add(new TripleShiftConstraintRequest(new EmployeeId("18L"), 50, true));
//        allRequests.add(new WeekendRequest(new EmployeeId("18L"), true, 50));
//        allRequests.add(new EmployeesPerShiftRequest(DAY, 4, 4, 0, 4, 0, 4));
//        allRequests.add(new EmployeesPerShiftRequest(NIGHT, 3, 3, 0, 3, 0, 3));
////        allRequests.add(new ShiftFollowUpRestrictionRequest(NIGHT, DAY, 0));
////        allRequests.add(new ConsecutiveWorkingDaysRequest(WORKING_SHIFTS, 0, 2, 1, 3, 50, 3));
//        return allRequests.stream()
//                .map(c -> {
//                    var record = new ConstraintRecord();
//                    record.setId(ConstraintId.random());
//                    record.setType(c.getType());
//                    record.setRequest(c);
//                    return record;
//                }).toList();
        return new ArrayList<>();
    }

    public static List<EmployeeRecord> employees() {
        return List.of(
                createEmployee("Martina", "Kravariková"),
                createEmployee("Vendula", "Zajícová"),
                createEmployee("Alena", "Janáková"),
                createEmployee("Simona", "Holmanová"),
                createEmployee("Jana", "Zelenková"),
                createEmployee("Jolana", "Pálffyová"),
                createEmployee("Nicola", "Halbichová"),
                createEmployee("Jana", "Kesslerová"),
                createEmployee("Eva", "Dudek Premauer"),
                createEmployee("Aneta", "Dubská"),
                createEmployee("Jindra", "Labounková"),
                createEmployee("Dana", "Zachová"),
                createEmployee("Iva", "Najmanová"),
                createEmployee("Barbora", "Řeháková"),
                createEmployee("Karolína", "Vavrušková"),
                createEmployee("Zuzana", "Kučerová"),
                createEmployee("Natálie", "Vejvodová"),
                createEmployee("Karolína", "Hromířová")
        );
    }

    private static EmployeeRecord createEmployee(String firstName, String lastName) {
        return new EmployeeRecord()
                .setId(EmployeeId.random().getId())
                .setFirstName(firstName)
                .setLastName(lastName);
    }

}
