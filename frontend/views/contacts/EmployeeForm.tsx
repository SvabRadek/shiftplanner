import { FormLayout } from "@hilla/react-components/FormLayout";
import { TextField } from "@hilla/react-components/TextField";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";

type Props = {
  employee: EmployeeRecord
}

export function EmployeeForm(props: Props) {

  const responsiveSteps = [
    { minWidth: "0", columns: 1 },
    { minWidth: "500px", columns: 6 },
  ]

  return (
    <FormLayout responsiveSteps={responsiveSteps}>
      <TextField label={"Jmeno"}
                 value={props.employee.firstName}
                 {...{ colspan: 3 }}
      />
      <TextField label={"Prijmeni"}
                 value={props.employee.lastName}
                 {...{ colspan: 3 }}
      />
    </FormLayout>
  );
}
