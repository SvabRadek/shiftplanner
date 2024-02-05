import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import { useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionRequestDTO";
import { ShiftSelect } from "Frontend/components/ShiftSelect";

type Props = {
  request: ShiftFollowupRestrictionRequestDTO
  onChange: (value: ShiftFollowupRestrictionRequestDTO) => void
}

export function ShiftFollowupRestrictionForm(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx);

  function handleUpdate(value: Partial<ShiftFollowupRestrictionRequestDTO>) {
    props.onChange({
      ...props.request,
      ...value
    })
  }

  return (
    <Card>
      <h6>Nastaveni zakazanych kombinaci smen</h6>
      <HorizontalLayout theme={"spacing"}>
        <ShiftSelect
          label={"Prvni smena"}
          selectedShift={props.request.firstShift}
          excludedShifts={[props.request.forbiddenFollowup]}
          onSelect={e => handleUpdate({
            firstShift: e
          })}/>
        <ShiftSelect
          label={"Nasledujici smena"}
          selectedShift={props.request.forbiddenFollowup}
          excludedShifts={[props.request.firstShift]}
          onSelect={e => handleUpdate({
            forbiddenFollowup: e
          })}/>
        <NumberField
          label={"Pokuta (0 = zakazano)"}
          value={props.request.penalty.toString()}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          onChange={e => handleUpdate({ penalty: Number.parseInt(e.target.value)})}
        />
      </HorizontalLayout>
    </Card>
  );
}
