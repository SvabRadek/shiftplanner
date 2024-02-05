import { Dialog, DialogOpenedChangedEvent } from "@hilla/react-components/Dialog";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import {
  ConsecutiveWorkingDaysForm
} from "Frontend/views/schedule/components/scheduleConstraints/ConsecutiveWorkingDaysForm";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ConsecutiveWorkingDaysRequestDTO";
import { Button } from "@hilla/react-components/Button";
import { CardFooter } from "Frontend/components/CardFooter";
import { useContext, useState } from "react";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftRequestDTO";
import { EmployeesPerShiftForm } from "Frontend/views/schedule/components/scheduleConstraints/EmployeesPerShiftForm";
import { areEmployeesPerShiftSame, areShiftFollowupRestrictionsSame } from "Frontend/util/utils";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionRequestDTO";
import {
  ShiftFollowupRestrictionForm
} from "Frontend/views/schedule/components/scheduleConstraints/ShiftFollowupRestrictionForm";

export type ScheduleConstraintDialogModel = {
  consecutiveWorkingDaysRequests: ConsecutiveWorkingDaysRequestDTO[]
  employeesPerShiftRequests: EmployeesPerShiftRequestDTO[]
  shiftFollowupRestrictionRequests: ShiftFollowupRestrictionRequestDTO[]
}

type Props = {
  isOpen: boolean
  onOpenChanged: (value: boolean) => void
  consecutiveWorkingDaysRequests: ConsecutiveWorkingDaysRequestDTO[]
  employeesPerShiftRequests: EmployeesPerShiftRequestDTO[]
  shiftFollowupRestrictionRequests: ShiftFollowupRestrictionRequestDTO[]
  onSave: (value: ScheduleConstraintDialogModel) => void
}

export function ScheduleConstraintsDialog(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx);

  const [scheduleConstraintModel, setScheduleConstraintModel] = useState<ScheduleConstraintDialogModel>({
    consecutiveWorkingDaysRequests: props.consecutiveWorkingDaysRequests,
    employeesPerShiftRequests: props.employeesPerShiftRequests,
    shiftFollowupRestrictionRequests: props.shiftFollowupRestrictionRequests
  });

  function handleOpenedChanged(e: DialogOpenedChangedEvent) {
    props.onOpenChanged(e.detail.value)
  }

  function handleConsecutiveFormChange(value: ConsecutiveWorkingDaysRequestDTO) {
    setScheduleConstraintModel(prevState => ({
      ...prevState,
      consecutiveWorkingDaysRequests: [value]
    }))
  }

  function handleEmployeesPerShiftChange(value: EmployeesPerShiftRequestDTO) {
    setScheduleConstraintModel(prevState => ({
      ...prevState,
      employeesPerShiftRequests: prevState.employeesPerShiftRequests.map(r => {
        if (!areEmployeesPerShiftSame(r, value)) return r
        return value
      })
    }))
  }

  function handleShiftFollowupRestrictionChange(value: ShiftFollowupRestrictionRequestDTO) {
    setScheduleConstraintModel(prevState => ({
      ...prevState,
      shiftFollowupRestrictionRequests: prevState.shiftFollowupRestrictionRequests.map(r => {
        if (!areShiftFollowupRestrictionsSame(r, value)) return r
        return value
      })
    }))
  }

  function handleClose() {
    props.onOpenChanged(false)
    setScheduleConstraintModel({
      consecutiveWorkingDaysRequests: props.consecutiveWorkingDaysRequests,
      employeesPerShiftRequests: props.employeesPerShiftRequests,
      shiftFollowupRestrictionRequests: props.shiftFollowupRestrictionRequests
    })
  }

  function handleSave() {
    props.onSave(scheduleConstraintModel);
  }

  return (
    <Dialog
      headerTitle={"Konfigurace rozvrhu"}
      opened={props.isOpen}
      onOpenedChanged={handleOpenedChanged}
      style={{ minWidth: "600px", minHeight: "300px" }}
    >
      <VerticalLayout theme={"spacing"}>
        {props.consecutiveWorkingDaysRequests.map((request) => (
          <ConsecutiveWorkingDaysForm
            key={"onlyone"}
            request={request}
            onChange={handleConsecutiveFormChange}
          />
        ))}
        {props.employeesPerShiftRequests.map(request => (
          <EmployeesPerShiftForm
            key={request.targetShift}
            request={request}
            excludedShifts={props.employeesPerShiftRequests.map(r => r.targetShift)}
            onChange={handleEmployeesPerShiftChange}
          />
        ))
        }
        {props.shiftFollowupRestrictionRequests.map(request => (
          <ShiftFollowupRestrictionForm
            key={request.firstShift + request.forbiddenFollowup}
            request={request}
            onChange={handleShiftFollowupRestrictionChange}
          />
        ))}
      </VerticalLayout>
      <CardFooter>
        <Button disabled={modeCtx.mode !== ScheduleMode.EDIT} onClick={handleSave}>
          Ulozit
        </Button>
        <Button onClick={handleClose}>
          Zrusit
        </Button>
      </CardFooter>
    </Dialog>
  );
}
