package com.cocroachden.planner.employee.fixtures;

import com.cocroachden.planner.employee.EmployeeDTO;

import java.util.List;

public class EmployeeFixturesData {
    public static List<EmployeeDTO> fixtureEmployees() {
        return List.of(
                new EmployeeDTO("31599ba4-52be-4fd7-bd30-11e600955bc4", "Martina", "Kravariková"),
                new EmployeeDTO("b20ab7e2-7e0c-42e7-81ac-ee2bd0af9332", "Vendula", "Zajícová"),
                new EmployeeDTO("65cf9557-84dc-4ef0-bf63-64163d10aff6", "Alena", "Janáková"),
                new EmployeeDTO("3e9dbf84-30a0-4200-bb8f-07041458365c", "Simona", "Holmanová"),
                new EmployeeDTO("85948388-e373-444c-bc5a-8eb07c8d253a", "Jana", "Zelenková"),
                new EmployeeDTO("8602e932-05ff-4512-87f2-81df80fae034", "Jolana", "Pálffyová"),
                new EmployeeDTO("54d698db-e4d9-41a1-a801-7976cf609de0", "Nicola", "Halbichová"),
                new EmployeeDTO("667e20d3-5ef3-4e9d-9d4d-fe30017d6ad1", "Jana", "Kesslerová"),
                new EmployeeDTO("0e79c179-6ffb-45f5-b39c-dfa4c784ba58", "Eva", "Dudek Premauer"),
                new EmployeeDTO("392663ae-a572-4da9-a65d-3f69ef717c81", "Aneta", "Dubská"),
                new EmployeeDTO("27ff4915-d34e-4df2-97b0-54963bb30c0d", "Jindra", "Labounková"),
                new EmployeeDTO("0ec23771-740d-47bb-9fc1-86422424b5f0", "Dana", "Zachová"),
                new EmployeeDTO("7acbeee7-264c-4b9f-bb2d-a3d77c98a2d1", "Iva", "Najmanová"),
                new EmployeeDTO("4d4ef171-ac6c-49e2-b0b2-dcfd62c63069", "Barbora", "Řeháková"),
                new EmployeeDTO("67f15171-2d80-4901-bbee-480e14ebee10", "Karolína", "Vavrušková"),
                new EmployeeDTO("284f5721-8935-4385-bbe3-d40d5345c7b3", "Zuzana", "Kučerová"),
                new EmployeeDTO("1efe54c1-c3e7-4d35-abde-f7dced1f06fb", "Natálie", "Vejvodová"),
                new EmployeeDTO("e085aabf-1051-449d-9164-b36b88799a9b", "Karolína", "Hromířová")
        );
    }
}
