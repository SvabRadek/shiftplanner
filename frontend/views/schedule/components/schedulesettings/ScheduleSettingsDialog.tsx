import { Dialog } from "@hilla/react-components/Dialog";
import { CSSProperties, useState } from "react";
import { Tab } from "@hilla/react-components/Tab";
import { Tabs } from "@hilla/react-components/Tabs";
import { EmployeeTab } from "Frontend/views/schedule/components/schedulesettings/EmployeeTab";
import { Button } from "@hilla/react-components/Button";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { GlobalTab } from "Frontend/views/schedule/components/schedulesettings/GlobalTab";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeesPerShiftRequestDTO";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ConsecutiveWorkingDaysRequestDTO";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftFollowupRestrictionRequestDTO";
import { CrudAction } from "Frontend/util/utils";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import SolverConfigurationDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverConfigurationDTO";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeDTO";
import AssignedEmployeeDTO from "Frontend/generated/com/cocroachden/planner/solver/api/AssignedEmployeeDTO";
import "@vaadin/icons";

type Props = {
  isOpen: boolean
  onOpenChanged: (value: boolean) => void
  employees: EmployeeDTO[]
  onAssignmentAction: (action: CrudAction<AssignedEmployeeDTO>) => void
  request: SolverConfigurationDTO
  consecutiveWorkingDays: ConsecutiveWorkingDaysRequestDTO[]
  onConsecutiveWorkingDaysAction: (action: CrudAction<ConsecutiveWorkingDaysRequestDTO>) => void
  employeesPerShift: EmployeesPerShiftRequestDTO[]
  onEmployeePerShiftAction: (a: CrudAction<EmployeesPerShiftRequestDTO>) => void
  shiftFollowupRestriction: ShiftFollowupRestrictionRequestDTO[]
  onShiftFollowupRestrictionAction: (a: CrudAction<ShiftFollowupRestrictionRequestDTO>) => void
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
      <VerticalLayout style={{ width: "75vw", maxWidth: "800px", height: "75vh" }}>
        <Tabs style={{ width: "100%" }}>
          <Tab onClick={() => setSelectedTab(0)}>Zamestnanci</Tab>
          <Tab onClick={() => setSelectedTab(1)}>Globalni</Tab>
        </Tabs>
        <div style={tabContainerCss}>
          {selectedTab == 0 &&
              <EmployeeTab onAssignmentAction={props.onAssignmentAction}
                           request={props.request}
                           employees={props.employees}/>
          }
          {selectedTab == 1 &&
              <GlobalTab employeesPerShift={props.employeesPerShift}
                         consecutiveWorkingDays={props.consecutiveWorkingDays}
                         shiftFollowupRestriction={props.shiftFollowupRestriction}
                         onEmployeePerShiftAction={props.onEmployeePerShiftAction}
                         onShiftFollowupRestrictionAction={props.onShiftFollowupRestrictionAction}
                         onConsecutiveWorkingDaysAction={props.onConsecutiveWorkingDaysAction}
              />
          }
        </div>
      </VerticalLayout>
      <HorizontalLayout theme={"spacing"} style={{ paddingTop: "20px", justifyContent: "end" }}>
        <Button onClick={() => props.onOpenChanged(false)}>Zavrit</Button>
      </HorizontalLayout>
    </Dialog>
  );
}
