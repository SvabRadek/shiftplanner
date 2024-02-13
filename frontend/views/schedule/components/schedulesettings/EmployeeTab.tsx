import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import { Icon } from "@hilla/react-components/Icon";
import { AddEmployeeDialog } from "Frontend/views/schedule/components/schedulesettings/AddEmployeeDialog";
import { useContext, useState } from "react";
import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";

type Props = {
  employees: EmployeeRecord[]
  request: PlannerConfigurationDTO
  onEmployeeAction: (action: CrudAction<EmployeeRecord>) => void
}

export function EmployeeTab(props: Props) {

  const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false);
  const { mode } = useContext(ScheduleModeCtx);
  const selectedWorkers = props.employees
    .filter(e => props.request.workers.some(w => w.workerId === e.workerId))
  const missingWorkers = props.employees
    .filter(e => !props.request.workers.some(w => w.workerId === e.workerId))

  function renderEmployeeCard(employee: EmployeeRecord) {
    return (
      <div key={employee.workerId}
           style={{
             width: "100%",
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
            <Button theme={"small icon"}
                    onClick={() =>
                      props.onEmployeeAction({ type: CRUDActions.READ, payload: employee })
                    }>
              <Icon icon={"vaadin:search"}/>
            </Button>
            <Button theme={"icon small"}
                    disabled={mode !== ScheduleMode.EDIT}
                    onClick={() =>
                      props.onEmployeeAction({ type: CRUDActions.DELETE, payload: employee })
                    }>
              <Icon icon={"vaadin:trash"}/>
            </Button>
          </HorizontalLayout>
        </HorizontalLayout>
      </div>
    )
  }


  return (
    <div>
      <HorizontalLayout theme={"spacing"}
                        style={{
                          width: "100%",
                          justifyContent: "start",
                          alignItems: "center",
                          paddingTop: 5,
                          paddingBottom: 5
                        }}>
        <AddEmployeeDialog employees={missingWorkers}
                           onOpenChanged={setIsAddEmployeeDialogOpen}
                           isOpen={isAddEmployeeDialogOpen}
                           onEmployeeAdd={e =>
                             props.onEmployeeAction({ type: CRUDActions.CREATE, payload: e })
                           }/>
        <Button theme={"icon"}
                disabled={mode !== ScheduleMode.EDIT}
                onClick={() => setIsAddEmployeeDialogOpen(true)}>
          <Icon icon={"vaadin:plus"}></Icon>
        </Button>
        <h6>Seznam zamestnancu</h6>
      </HorizontalLayout>
      <div style={{ width: "98%" }}>
        {selectedWorkers.map(w => renderEmployeeCard(w))}
      </div>
    </div>

  );
}
