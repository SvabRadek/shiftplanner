import {CustomEmployeeEndpoint} from "Frontend/generated/endpoints";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {useEffect, useState} from "react";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import {EmployeeCard} from "Frontend/views/employees/components/EmployeeCard";

export function EmployeeView2() {

    const [employees, setEmployees] = useState<EmployeeDTO[]>([]);

    useEffect(() => {
        CustomEmployeeEndpoint.getAllEmployees().then(setEmployees)
    }, []);


    return (
      <VerticalLayout theme={"spacing padding"}>
          {employees.map(e => <EmployeeCard employee={e} onEmployeeAction={() => {}}/>)}
      </VerticalLayout>
  );
}