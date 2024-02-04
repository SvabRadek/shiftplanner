import { Dialog, DialogOpenedChangedEvent } from "@hilla/react-components/Dialog";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import {
  ConsecutiveWorkingDaysForm
} from "Frontend/views/schedule/components/scheduleConstraints/ConsecutiveWorkingDaysForm";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ConsecutiveWorkingDaysRequestDTO";
import { Button } from "@hilla/react-components/Button";
import { CardFooter } from "Frontend/components/CardFooter";
import { useState } from "react";

export type ScheduleConstraintDialogModel = {
  consecutiveWorkingDaysRequests: ConsecutiveWorkingDaysRequestDTO[]
}

type Props = {
  isOpen: boolean
  onOpenChanged: (value: boolean) => void
  consecutiveWorkingDaysRequests: ConsecutiveWorkingDaysRequestDTO[]
  onSave: (value: ScheduleConstraintDialogModel) => void
}

export function ScheduleConstraintsDialog(props: Props) {

  const [scheduleConstraintModel, setScheduleConstraintModel] = useState<ScheduleConstraintDialogModel>({
    consecutiveWorkingDaysRequests: props.consecutiveWorkingDaysRequests
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

  function handleClose() {
    props.onOpenChanged(false)
    setScheduleConstraintModel({
      consecutiveWorkingDaysRequests: props.consecutiveWorkingDaysRequests
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
      </VerticalLayout>
      <CardFooter>
        <Button onClick={handleSave}>
          Ulozit
        </Button>
        <Button onClick={handleClose}>
          Zrusit
        </Button>
      </CardFooter>
    </Dialog>
  );
}
