import { Dialog } from "@hilla/react-components/Dialog";
import { useState } from "react";
import { Tab } from "@hilla/react-components/Tab";
import { Tabs } from "@hilla/react-components/Tabs";
import { EmployeeTab } from "Frontend/views/schedule/components/schedulesettings/EmployeeTab";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import { Button } from "@hilla/react-components/Button";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { GlobalTab } from "Frontend/views/schedule/components/schedulesettings/GlobalTab";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftRequestDTO";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ConsecutiveWorkingDaysRequestDTO";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionRequestDTO";
import { CrudAction } from "Frontend/util/utils";

type Props = {
  isOpen: boolean
  onOpenChanged: (value: boolean) => void
  employees: EmployeeRecord[]
  onEmployeeAction: (action: CrudAction<EmployeeRecord>) => void
  request: PlannerConfigurationDTO
  consecutiveWorkingDays: ConsecutiveWorkingDaysRequestDTO[]
  onConsecutiveWorkingDaysAction: (action: CrudAction<ConsecutiveWorkingDaysRequestDTO>) => void
  employeesPerShift: EmployeesPerShiftRequestDTO[]
  onEmployeePerShiftAction: (a: CrudAction<EmployeesPerShiftRequestDTO>) => void
  shiftFollowupRestriction: ShiftFollowupRestrictionRequestDTO[]
  onShiftFollowupRestrictionAction: (a: CrudAction<ShiftFollowupRestrictionRequestDTO>) => void
}

export function ScheduleSettingsDialog(props: Props) {

  const [selectedTab, setSelectedTab] = useState(0);

  return (
    <Dialog
      headerTitle={"Konfigurace rozvrhu"}
      opened={props.isOpen}
      onOpenedChanged={e => props.onOpenChanged(e.detail.value)}
    >
      <div style={{ minWidth: "50vw", minHeight: "75vh" }}>
        <Tabs style={{ width: "100%" }}>
          <Tab onClick={() => setSelectedTab(0)}>Zamestnanci</Tab>
          <Tab onClick={() => setSelectedTab(1)}>Globalni</Tab>
        </Tabs>
        {selectedTab == 0 &&
            <EmployeeTab onEmployeeAction={props.onEmployeeAction}
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
      <HorizontalLayout theme={"spacing"} style={{ paddingTop: "20px", justifyContent: "end" }}>
        <Button onClick={() => props.onOpenChanged(false)}>Zavrit</Button>
      </HorizontalLayout>
    </Dialog>
  );
}
