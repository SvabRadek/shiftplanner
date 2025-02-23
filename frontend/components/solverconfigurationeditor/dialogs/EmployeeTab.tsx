import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {Button} from "@hilla/react-components/Button";
import {Icon} from "@hilla/react-components/Icon";
import {AddEmployeeDialog} from "Frontend/components/solverconfigurationeditor/dialogs/AddEmployeeDialog";
import {useState} from "react";
import {CrudAction, CRUDActions} from "Frontend/util/utils";
import "@vaadin/icons";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";

type Props = {
    employees: EmployeeDTO[]
    request: SolverConfigurationDTO
    onAssignmentAction: (action: CrudAction<EmployeeAssignmentDTO>) => void
    readonly: boolean
}

export function EmployeeTab(props: Props) {

    const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false);
    const selectedAssignments = props.request.employees
        .sort((a, b) => a.index - b.index)
    const missingEmployees = props.employees
        .filter(e => !props.request.employees.some(a1 => e.id === a1.employeeId))

    function renderEmployeeCard(assignment: EmployeeAssignmentDTO, employee: EmployeeDTO) {
        return (
            <div key={assignment.employeeId}
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
                            disabled={props.readonly || assignment.index < 1}
                            theme={"icon small"}
                            onClick={() => props.onAssignmentAction({
                                type: CRUDActions.UPDATE,
                                payload: {...assignment, index: assignment.index - 1}
                            })}>
                            <Icon icon={"vaadin:chevron-up"}></Icon>
                        </Button>
                        <Button
                            disabled={props.readonly || assignment.index + 1 >= props.request.employees.length}
                            theme={"icon small"}
                            onClick={() => props.onAssignmentAction({
                                type: CRUDActions.UPDATE,
                                payload: {...assignment, index: assignment.index + 1}
                            })}
                        >
                            <Icon icon={"vaadin:chevron-down"}></Icon>
                        </Button>
                    </HorizontalLayout>
                    <div style={{paddingLeft: "var(--lumo-space-l)", width: "100%"}}>
                        {employee.lastName + " " + employee.firstName}
                    </div>
                    <HorizontalLayout theme={"spacing"}>
                        <Button theme={"small icon"}
                                onClick={() => props.onAssignmentAction({type: CRUDActions.READ, payload: assignment})}>
                            <Icon icon={"vaadin:search"}/>
                        </Button>
                        <Button theme={"icon small"}
                                disabled={props.readonly}
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
                        disabled={props.readonly}
                        onClick={() => setIsAddEmployeeDialogOpen(true)}>
                    <Icon icon={"vaadin:plus"}></Icon>
                </Button>
                <h6>Seznam zaměstnanců</h6>
            </HorizontalLayout>
            <div style={{width: "98%"}}>
                {selectedAssignments.map(w => renderEmployeeCard(w, props.employees.find(e => e.id === w.employeeId)!))}
            </div>
        </div>

    );
}
