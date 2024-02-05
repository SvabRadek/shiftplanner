import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftRequestDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { ShiftSelect } from "Frontend/components/ShiftSelect";

type Props = {
  request: EmployeesPerShiftRequestDTO
  excludedShifts: WorkShifts[]
  onChange: (value: EmployeesPerShiftRequestDTO) => void
}

export function EmployeesPerShiftForm(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx);

  function handleUpdate(value: Partial<EmployeesPerShiftRequestDTO>) {
    props.onChange({
      ...props.request,
      ...value
    })
  }

  return (
    <Card>
      <h6>Pocet zamestancu prirazenych na smenu</h6>
      <HorizontalLayout theme={"spacing"} style={{ alignItems: "center" }}>
        <ShiftSelect
          label={"Smena"}
          selectedShift={props.request.targetShift}
          onSelect={e => handleUpdate({ targetShift: e })}
        />
        <NumberField
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Min"}
          value={props.request.hardMin.toString()}
          style={{ width: "100px" }}
          onChange={e => handleUpdate({ hardMin: Number.parseInt(e.target.value) })}
        />
        <NumberField
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Soft Min"}
          style={{ width: "100px" }}
          value={props.request.softMin.toString()}
          onChange={e => handleUpdate({ softMin: Number.parseInt(e.target.value) })}
        />
        <NumberField
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Penalty"}
          style={{ width: "100px" }}
          value={props.request.minPenalty.toString()}
          onChange={e => handleUpdate({ minPenalty: Number.parseInt(e.target.value) })}
        />
        <NumberField
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Soft Max"}
          style={{ width: "100px" }}
          value={props.request.softMax.toString()}
          onChange={e => handleUpdate({ softMax: Number.parseInt(e.target.value) })}
        />
        <NumberField
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Hard Max"}
          style={{ width: "100px" }}
          value={props.request.hardMax.toString()}
          onChange={e => handleUpdate({ hardMax: Number.parseInt(e.target.value) })}
        />
        <NumberField
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          label={"Penalty"}
          style={{ width: "100px" }}
          value={props.request.maxPenalty.toString()}
          onChange={e => handleUpdate({ maxPenalty: Number.parseInt(e.target.value) })}
        />
      </HorizontalLayout>
    </Card>
  );
}
