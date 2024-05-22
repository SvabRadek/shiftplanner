import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeesPerShiftRequestDTO";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ConsecutiveWorkingDaysRequestDTO";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftFollowupRestrictionRequestDTO";
import { EmployeesPerShiftForm } from "Frontend/views/schedule/components/schedulesettings/constraintform/EmployeesPerShiftForm";
import {
  ShiftFollowupRestrictionForm
} from "Frontend/views/schedule/components/schedulesettings/constraintform/ShiftFollowupRestrictionForm";
import { CrudAction, CRUDActions, generateUUID } from "Frontend/util/utils";
import {
  ConsecutiveWorkingDaysForm
} from "Frontend/views/schedule/components/schedulesettings/constraintform/ConsecutiveWorkingDaysForm";
import { CSSProperties, useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { defaultConstraints } from "Frontend/views/schedule/DefaultEmptyConstraints";

type Props = {
  consecutiveWorkingDays: ConsecutiveWorkingDaysRequestDTO[]
  onConsecutiveWorkingDaysAction: (action: CrudAction<ConsecutiveWorkingDaysRequestDTO>) => void
  shiftFollowupRestriction: ShiftFollowupRestrictionRequestDTO[]
  onShiftFollowupRestrictionAction: (a: CrudAction<ShiftFollowupRestrictionRequestDTO>) => void
  employeesPerShift: EmployeesPerShiftRequestDTO[]
  onEmployeePerShiftAction: (a: CrudAction<EmployeesPerShiftRequestDTO>) => void
}

export function GlobalTab(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx);

  const sectionStyle: CSSProperties = {
    width: "100%",
    justifyContent: "start",
    alignItems: "center",
    paddingTop: 10
  }

  return (
    <Card theme={"spacing padding"}>
      <HorizontalLayout theme={"spacing"} style={sectionStyle}>
        <Button
          onClick={() => props.onEmployeePerShiftAction({
            type: CRUDActions.CREATE,
            payload: { ...defaultConstraints.WORKERS_PER_SHIFT.constraint, id: generateUUID() } as unknown as EmployeesPerShiftRequestDTO
          })}
          disabled={modeCtx.mode !== ScheduleMode.EDIT}
          theme={"small icon"}>
          <Icon icon={"vaadin:plus"}/>
        </Button>
        <h6>Pocet zamestnancu pridelenych na smenu</h6>
      </HorizontalLayout>
      {props.employeesPerShift.map(r => (
        <EmployeesPerShiftForm key={r.id}
                               request={r}
                               excludedShifts={props.employeesPerShift.map(r => r.targetShift)}
                               onAction={props.onEmployeePerShiftAction}/>
      ))}
      <HorizontalLayout theme={"spacing"} style={sectionStyle}>
        <Button onClick={() => props.onShiftFollowupRestrictionAction({
          type: CRUDActions.CREATE,
          payload: { ...defaultConstraints.SHIFT_FOLLOW_UP_RESTRICTION.constraint, id: generateUUID() } as unknown as ShiftFollowupRestrictionRequestDTO
        })} disabled={modeCtx.mode !== ScheduleMode.EDIT} theme={"small icon"}>
          <Icon icon={"vaadin:plus"}/>
        </Button>
        <h6>Nastaveni zakazanych kombinaci smen</h6>
      </HorizontalLayout>
      {props.shiftFollowupRestriction.map(r => (
        <ShiftFollowupRestrictionForm key={r.firstShift + r.forbiddenFollowup}
                                      request={r}
                                      onAction={props.onShiftFollowupRestrictionAction}/>
      ))}
      <HorizontalLayout theme={"spacing"} style={sectionStyle}>
        <Button
          onClick={() => props.onConsecutiveWorkingDaysAction({
            type: CRUDActions.CREATE,
            payload: { ...defaultConstraints.CONSECUTIVE_WORKING_DAYS.constraint, id: generateUUID() } as unknown as ConsecutiveWorkingDaysRequestDTO
          })}
          disabled={modeCtx.mode !== ScheduleMode.EDIT}
          theme={"small icon"}>
          <Icon icon={"vaadin:plus"}/>
        </Button>
        <h6>Nastaveni poctu po sobe jdoucich smen</h6>
      </HorizontalLayout>
      {props.consecutiveWorkingDays.map(r => (
        <ConsecutiveWorkingDaysForm key={r.id}
                                    request={r}
                                    onAction={props.onConsecutiveWorkingDaysAction}/>
      ))}
    </Card>
  );
}
