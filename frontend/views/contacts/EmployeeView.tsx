import { useEffect, useState } from "react";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeService/EmployeeRecord";
import { EmployeeService } from "Frontend/generated/endpoints";
import { Grid } from "@hilla/react-components/Grid";
import { GridColumn } from "@hilla/react-components/GridColumn";

export default function EmployeeView() {

  const [employees, setEmployees] = useState<EmployeeRecord[]>([])
  const [selectedEmployees, setSelectedEmployees] = useState<EmployeeRecord[]>([])

  useEffect(() => {
    EmployeeService.getEmployees().then(setEmployees)
  }, []);

  return (
    <div className="p-m">
      <h2>Zamestnanci</h2>
      <Grid items={employees}
            selectedItems={selectedEmployees}
            onActiveItemChanged={e => {
              const item = e.detail.value
              setSelectedEmployees(item ? [item] : [])
            }}
      >
        <GridColumn path={"id"}/>
        <GridColumn path={"firstName"}/>
        <GridColumn path={"lastName"}/>
      </Grid>
    </div>
  );
}
