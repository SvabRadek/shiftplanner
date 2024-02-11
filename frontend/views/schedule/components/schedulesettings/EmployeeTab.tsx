import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import { Icon } from "@hilla/react-components/Icon";
import { AddEmployeeDialog } from "Frontend/views/schedule/components/schedulesettings/AddEmployeeDialog";
import { useState } from "react";
import { CrudAction, CRUDActions } from "Frontend/util/utils";

type Props = {
  employees: EmployeeRecord[]
  request: PlannerConfigurationDTO
  onEmployeeAction: (action: CrudAction<EmployeeRecord>) => void
}

export function EmployeeTab(props: Props) {

  const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false);

  const selectedWorkers = props.employees
    .filter(e => props.request.workers.some(w => w.workerId === e.workerId))
  const missingWorkers = props.employees
    .filter(e => !props.request.workers.some(w => w.workerId === e.workerId))

  function renderEmployeeCard(employee: EmployeeRecord) {
    return (
      <div key={employee.workerId}
           style={{
             width: "97%",
             marginBottom: "5px",
             backgroundColor: "var(--lumo-shade-10pct)",
             padding: "5px",
             borderRadius: "5px"
           }}>
        <HorizontalLayout
          style={{
            width: "100%",
            alignItems: "center",
            justifyContent: "space-between",
            paddingLeft: "2%",
            paddingRight: "5px"
          }}>
          <div>
            {employee.firstName + " " + employee.lastName}
          </div>
          <HorizontalLayout theme={"spacing"}>
            <Button theme={"small icon"} onClick={() =>
              props.onEmployeeAction({ type: CRUDActions.READ, payload: employee })
            }>
              <Icon icon={"vaadin:search"}/>
            </Button>
            <Button theme={"icon small"} onClick={() =>
              props.onEmployeeAction({ type: CRUDActions.CREATE, payload: employee })
            }>
              <Icon icon={"vaadin:trash"}/>
            </Button>
          </HorizontalLayout>
        </HorizontalLayout>
      </div>
    )
  }


  return (
    <Card style={{ maxHeight: "75vh" }}>
      <HorizontalLayout theme={"spacing"} style={{ width: "100%", justifyContent: "start" }}>
        <div>
          <AddEmployeeDialog employees={missingWorkers}
                             onOpenChanged={setIsAddEmployeeDialogOpen}
                             isOpen={isAddEmployeeDialogOpen}
                             onEmployeeAdd={e =>
                               props.onEmployeeAction({ type: CRUDActions.CREATE, payload: e })
                             }
          />
          <Button onClick={() => setIsAddEmployeeDialogOpen(true)}>
            <Icon icon={"vaadin:plus"} slot={"prefix"}></Icon>
            Pridat
          </Button>
        </div>
      </HorizontalLayout>
      <div style={{ width: "100%", overflowY: "scroll" }}>
        {selectedWorkers.map(w => renderEmployeeCard(w))}
      </div>
    </Card>
  );
}
