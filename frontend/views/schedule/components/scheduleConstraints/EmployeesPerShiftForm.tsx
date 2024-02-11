import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftRequestDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { ShiftSelect } from "Frontend/components/ShiftSelect";
import { CrudAction, CRUDActions } from "Frontend/util/utils";

type Props = {
  request: EmployeesPerShiftRequestDTO
  excludedShifts: WorkShifts[]
  onAction: (action: CrudAction<EmployeesPerShiftRequestDTO>) => void
}

export function EmployeesPerShiftForm(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx);

  function handleUpdate(value: Partial<EmployeesPerShiftRequestDTO>) {
    props.onAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.request,
        ...value
      }
    })
  }

  return (
    <Card>
      <HorizontalLayout theme={"spacing"} style={{ alignItems: "center" }}>
        <ShiftSelect
          theme={"small"}
          label={"Smena"}
          selectedShift={props.request.targetShift}
          onSelect={e => handleUpdate({ targetShift: e })}
        />
        <NumberField
          theme={"small"}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Min"}
          value={props.request.hardMin.toString()}
          style={{ width: "50px" }}
          onChange={e => handleUpdate({ hardMin: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Soft Min"}
          style={{ width: "75px" }}
          value={props.request.softMin.toString()}
          onChange={e => handleUpdate({ softMin: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Pokuta"}
          style={{ width: "50px" }}
          value={props.request.minPenalty.toString()}
          onChange={e => handleUpdate({ minPenalty: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Soft Max"}
          style={{ width: "75px" }}
          value={props.request.softMax.toString()}
          onChange={e => handleUpdate({ softMax: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Max"}
          style={{ width: "50px" }}
          value={props.request.hardMax.toString()}
          onChange={e => handleUpdate({ hardMax: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Pokuta"}
          style={{ width: "50px" }}
          value={props.request.maxPenalty.toString()}
          onChange={e => handleUpdate({ maxPenalty: Number.parseInt(e.target.value) })}
        />
      </HorizontalLayout>
    </Card>
  );
}
