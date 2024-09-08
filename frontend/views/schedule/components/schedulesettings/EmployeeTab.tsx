import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { AddEmployeeDialog } from "Frontend/views/schedule/components/schedulesettings/AddEmployeeDialog";
import { useContext, useState } from "react";
import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import SolverConfigurationDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverConfigurationDTO";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeDTO";
import AssignedEmployeeDTO from "Frontend/generated/com/cocroachden/planner/solver/api/AssignedEmployeeDTO";
import "@vaadin/icons";

type Props = {
  employees: EmployeeDTO[]
  request: SolverConfigurationDTO
  onAssignmentAction: (action: CrudAction<AssignedEmployeeDTO>) => void
}

export function EmployeeTab(props: Props) {

  const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false);
  const { mode } = useContext(ScheduleModeCtx);
  const selectedAssignments = props.request.employees
    .sort((a, b) => a.index - b.index)
  const missingEmployees = props.employees
    .filter(e => !props.request.employees.some(a1 => e.id === a1.employee.id))

  function renderEmployeeCard(assignment: AssignedEmployeeDTO) {
    return (
      <div key={assignment.employee.id}
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
          <HorizontalLayout theme={"spacing"}>
            <Button
              disabled={mode !== ScheduleMode.EDIT || assignment.index < 1}
              theme={"icon small"}
              onClick={() => props.onAssignmentAction({
                type: CRUDActions.UPDATE,
                payload: { ...assignment, index: assignment.index - 1 }
              })}>
              <Icon icon={"vaadin:chevron-up"}></Icon>
            </Button>
            <Button
              disabled={mode !== ScheduleMode.EDIT || assignment.index + 1 >= props.request.employees.length}
              theme={"icon small"}
              onClick={() => props.onAssignmentAction({
                type: CRUDActions.UPDATE,
                payload: { ...assignment, index: assignment.index + 1 }
              })}
            >
              <Icon icon={"vaadin:chevron-down"}></Icon>
            </Button>
          </HorizontalLayout>
          <div style={{ paddingLeft: "var(--lumo-space-l)", width: "100%" }}>
            {assignment.employee.lastName + " " + assignment.employee.firstName}
          </div>
          <HorizontalLayout theme={"spacing"}>
            <Button theme={"small icon"}
                    onClick={() => props.onAssignmentAction({ type: CRUDActions.READ, payload: assignment })}>
              <Icon icon={"vaadin:search"}/>
            </Button>
            <Button theme={"icon small"}
                    disabled={mode !== ScheduleMode.EDIT}
                    onClick={() => props.onAssignmentAction({
                      type: CRUDActions.DELETE,
                      payload: assignment
                    })}>
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
        <AddEmployeeDialog employees={missingEmployees}
                           onOpenChanged={setIsAddEmployeeDialogOpen}
                           isOpen={isAddEmployeeDialogOpen}
                           onAssignmentAdd={partialAssignment =>
                             props.onAssignmentAction({
                               type: CRUDActions.CREATE, payload: {
                                 ...partialAssignment,
                                 index: props.request.employees.length
                               }
                             })
                           }/>
        <Button theme={"icon"}
                disabled={mode !== ScheduleMode.EDIT}
                onClick={() => setIsAddEmployeeDialogOpen(true)}>
          <Icon icon={"vaadin:plus"}></Icon>
        </Button>
        <h6>Seznam zaměstnanců</h6>
      </HorizontalLayout>
      <div style={{ width: "98%" }}>
        {selectedAssignments.map(w => renderEmployeeCard(w))}
      </div>
    </div>

  );
}
