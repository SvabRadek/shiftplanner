import { useEffect, useState } from "react";
import { Grid } from "@hilla/react-components/Grid";
import { GridColumn } from "@hilla/react-components/GridColumn";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { EmployeeService } from "Frontend/generated/endpoints";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { EmployeeForm } from "Frontend/views/contacts/EmployeeForm";

export default function EmployeeView() {

  const [employees, setEmployees] = useState<EmployeeRecord[]>([])
  const [selectedEmployee, setSelectedEmployee] = useState<EmployeeRecord | undefined>(undefined)

  useEffect(() => {
    EmployeeService.getAllEmployees().then(setEmployees)
  }, []);

  return (
    <div className="p-m">
      <VerticalLayout theme={"spacing padding"}>
        <h2>Zamestnanci</h2>
        <Grid items={employees}
              selectedItems={selectedEmployee ? [selectedEmployee] : []}
              onActiveItemChanged={e => {
                const item = e.detail.value
                setSelectedEmployee(item ? item : undefined)
              }}
        >
          <GridColumn path={"id"}/>
          <GridColumn path={"firstName"}/>
          <GridColumn path={"lastName"}/>
        </Grid>
        {selectedEmployee ? <EmployeeForm employee={selectedEmployee}/> : null}
      </VerticalLayout>
    </div>
  );
}
