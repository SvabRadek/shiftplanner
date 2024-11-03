import {Dialog} from "@hilla/react-components/Dialog";
import {CSSProperties, useState} from "react";
import {Tab} from "@hilla/react-components/Tab";
import {Tabs} from "@hilla/react-components/Tabs";
import {EmployeeTab} from "Frontend/components/solverconfigurationeditor/dialogs/EmployeeTab";
import {Button} from "@hilla/react-components/Button";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {GlobalTab} from "Frontend/components/solverconfigurationeditor/dialogs/GlobalTab";
import {CrudAction} from "Frontend/util/utils";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import "@vaadin/icons";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import {SortedConstraints} from "Frontend/views/schedule/ConstraintUtils";
import ConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintDTO";

type Props = {
    isOpen: boolean
    onOpenChanged: (value: boolean) => void
    employees: EmployeeDTO[]
    onAssignmentAction: (action: CrudAction<EmployeeAssignmentDTO>) => void
    solverConfiguration: SolverConfigurationDTO
    sortedConstraints: SortedConstraints
    onConstraintAction: (action: CrudAction<ConstraintDTO>) => void
    readonly: boolean
}

export function ScheduleSettingsDialog(props: Props) {

    const [selectedTab, setSelectedTab] = useState(0);
    const tabContainerCss: CSSProperties = {
        paddingLeft: 5,
        paddingRight: 5,
        width: "100%",
        maxHeight: "75vh",
        overflowY: "scroll"
    }
    return (
        <Dialog
            headerTitle={"Konfigurace rozvrhu"}
            opened={props.isOpen}
            onOpenedChanged={e => props.onOpenChanged(e.detail.value)}
        >
            <VerticalLayout style={{width: "75vw", maxWidth: "800px", height: "75vh"}}>
                <Tabs style={{width: "100%"}}>
                    <Tab onClick={() => setSelectedTab(0)}>Zamestnanci</Tab>
                    <Tab onClick={() => setSelectedTab(1)}>Globalni</Tab>
                </Tabs>
                <div style={tabContainerCss}>
                    {selectedTab == 0 &&
                        <EmployeeTab
                            onAssignmentAction={props.onAssignmentAction}
                            request={props.solverConfiguration}
                            employees={props.employees}
                            readonly={props.readonly}
                        />
                    }
                    {selectedTab == 1 &&
                        <GlobalTab
                            sortedConstraints={props.sortedConstraints}
                            onConstraintAction={props.onConstraintAction}
                            readonly={props.readonly}
                        />
                    }
                </div>
            </VerticalLayout>
            <HorizontalLayout theme={"spacing"} style={{paddingTop: "20px", justifyContent: "end"}}>
                <Button onClick={() => props.onOpenChanged(false)}>Zavrit</Button>
            </HorizontalLayout>
        </Dialog>
    );
}
