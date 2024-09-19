import { Select, SelectItem } from "@hilla/react-components/Select";
import { workShiftBindings } from "Frontend/views/schedule/WorkShiftBindigs";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { CSSProperties, useContext } from "react";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";

type Props = {
  label: string
  selectedShift: WorkShifts
  onSelect: (value: WorkShifts) => void
  excludedShifts?: WorkShifts[]
  style?: CSSProperties
  theme?: string
}

export function ShiftSelect(props: Props) {
  const modeCtx = useContext(ScheduleModeCtx);

  const selectItems: SelectItem[] =
    Object.values(workShiftBindings)
      .map(binding => ({
        label: binding.fullText,
        value: binding.shift,
        disabled: props.selectedShift !== binding.shift && props.excludedShifts?.some(s => s === binding.shift)
      }))

  return (
    <Select
      label={props.label}
      theme={props.theme}
      style={{ ...props.style, width: props.style?.width ?? "200px" }}
      items={selectItems}
      readonly={modeCtx.mode !== ScheduleMode.EDIT}
      value={props.selectedShift}
      onChange={(e) => props.onSelect(e.target.value as WorkShifts)}
    />
  );
}
