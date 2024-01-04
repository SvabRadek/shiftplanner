import { FormLayout } from "@hilla/react-components/FormLayout";
import { TextField } from "@hilla/react-components/TextField";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { IntegerField } from "@hilla/react-components/IntegerField";

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
      <IntegerField label={"Pocet smen"}
                    {...{ colspan: 2 }}
      />
      <IntegerField label={"Min"}
                    {...{ colspan: 2 }}
                    stepButtonsVisible
      />
      <IntegerField label={"Max"}
                    {...{ colspan: 2 }}
                    stepButtonsVisible
      />
    </FormLayout>
  );
}
