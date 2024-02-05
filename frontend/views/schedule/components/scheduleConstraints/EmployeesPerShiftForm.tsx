import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftRequestDTO";
import { Select, SelectItem } from "@hilla/react-components/Select";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { workShiftBindings } from "Frontend/views/schedule/WorkShiftBindigs";
import { useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";

type Props = {
  request: EmployeesPerShiftRequestDTO
  excludedShifts: WorkShifts[]
  onChange: (value: EmployeesPerShiftRequestDTO) => void
}

export function EmployeesPerShiftForm(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx);

  const selectItems: SelectItem[] =
    Object.values(workShiftBindings)
      .map(binding => ({
        label: binding.fullText,
        value: binding.shift,
        disabled: props.request.targetShift !== binding.shift && props.excludedShifts.some(s => s === binding.shift)
      }))


  function handleUpdate(value: Partial<EmployeesPerShiftRequestDTO>) {
    props.onChange({
      ...props.request,
      ...value
    })
  }

  function renderWorkShiftSelect() {
    return (
      <Select
        label={"Smena"}
        style={{ width: "200px" }}
        items={selectItems}
        readonly={modeCtx.mode !== ScheduleMode.EDIT}
        value={props.request.targetShift}
        onChange={(e) => handleUpdate({ targetShift: e.target.value as WorkShifts })}
      >
      </Select>
    )
  }

  return (
    <Card>
      <h6>Pocet zamestancu prirazenych na smenu</h6>
      <HorizontalLayout theme={"spacing"} style={{ alignItems: "center" }}>
        {renderWorkShiftSelect()}
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
