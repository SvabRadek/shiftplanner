import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftRequestDTO";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ConsecutiveWorkingDaysRequestDTO";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionRequestDTO";
import { EmployeesPerShiftForm } from "Frontend/views/schedule/components/scheduleConstraints/EmployeesPerShiftForm";
import {
  ShiftFollowupRestrictionForm
} from "Frontend/views/schedule/components/scheduleConstraints/ShiftFollowupRestrictionForm";
import { CrudAction } from "Frontend/util/utils";
import {
  ConsecutiveWorkingDaysForm
} from "Frontend/views/schedule/components/scheduleConstraints/ConsecutiveWorkingDaysForm";
import { CSSProperties } from "react";

type Props = {
  consecutiveWorkingDays: ConsecutiveWorkingDaysRequestDTO[]
  onConsecutiveWorkingDaysAction: (action: CrudAction<ConsecutiveWorkingDaysRequestDTO>) => void
  shiftFollowupRestriction: ShiftFollowupRestrictionRequestDTO[]
  onShiftFollowupRestrictionAction: (a: CrudAction<ShiftFollowupRestrictionRequestDTO>) => void
  employeesPerShift: EmployeesPerShiftRequestDTO[]
  onEmployeePerShiftAction: (a: CrudAction<EmployeesPerShiftRequestDTO>) => void
}

export function GlobalTab(props: Props) {

  const sectionStyle: CSSProperties = {
    width: "100%",
    justifyContent: "space-between",
    alignItems: "center",
    paddingTop: 10
  }

  return (
    <Card theme={"spacing padding"} style={{ maxHeight: "75vh" }}>
      <HorizontalLayout theme={"spacing"} style={sectionStyle}>
        <h6>Pocet zamestnancu pridelenych na smenu</h6>
        <Button theme={"small icon"}>
          <Icon icon={"vaadin:plus"}/>
        </Button>
      </HorizontalLayout>
      {props.employeesPerShift.map(r => (
        <EmployeesPerShiftForm key={r.targetShift}
                               request={r}
                               excludedShifts={props.employeesPerShift.map(r => r.targetShift)}
                               onAction={props.onEmployeePerShiftAction}/>
      ))}
      <HorizontalLayout theme={"spacing"} style={sectionStyle}>
        <h6>Nastaveni zakazanych kombinaci smen</h6>
        <Button theme={"small icon"}>
          <Icon icon={"vaadin:plus"}/>
        </Button>
      </HorizontalLayout>
      {props.shiftFollowupRestriction.map(r => (
        <ShiftFollowupRestrictionForm key={r.firstShift + r.forbiddenFollowup}
                                      request={r}
                                      onAction={props.onShiftFollowupRestrictionAction}/>
      ))}
      <HorizontalLayout theme={"spacing"} style={sectionStyle}>
        <h6>Nastaveni zakazanych kombinaci smen</h6>
        <Button theme={"small icon"}>
          <Icon icon={"vaadin:plus"}/>
        </Button>
      </HorizontalLayout>
      {props.consecutiveWorkingDays.map(r => (
        <ConsecutiveWorkingDaysForm key={r.type}
                                    request={r}
                                    onAction={props.onConsecutiveWorkingDaysAction}/>
      ))}
    </Card>
  );
}
