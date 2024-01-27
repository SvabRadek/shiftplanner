import { Dialog } from "@hilla/react-components/Dialog";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { TextField } from "@hilla/react-components/TextField";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";

type Props = {
  employee?: EmployeeRecord,
  isOpen: boolean
  onOpenChanged: (value: boolean) => void
}

export function EmployeeRequestConfig(props: Props) {

  if (!props.employee) return null

  return (
    <Dialog
      header-title={"Konfigurace zamestance"}
      opened={props.isOpen}
      onOpenedChanged={e => {
        props.onOpenChanged(e.detail.value)
      }}
    >
      <VerticalLayout theme={"spacing padding"}>
        <h6>Jmeno</h6>
        <HorizontalLayout theme={"spacing"}>
          <TextField value={props.employee.firstName} readonly/>
          <TextField value={props.employee.lastName} readonly/>
        </HorizontalLayout>
        <Button>Pridat Omezeni</Button>
      </VerticalLayout>
    </Dialog>
  );
}
