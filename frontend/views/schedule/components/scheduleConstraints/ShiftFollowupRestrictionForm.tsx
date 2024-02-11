import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import { useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionRequestDTO";
import { ShiftSelect } from "Frontend/components/ShiftSelect";
import { CrudAction, CRUDActions } from "Frontend/util/utils";

type Props = {
  request: ShiftFollowupRestrictionRequestDTO
  onAction: (action: CrudAction<ShiftFollowupRestrictionRequestDTO>) => void
}

export function ShiftFollowupRestrictionForm(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx);

  function handleUpdate(value: Partial<ShiftFollowupRestrictionRequestDTO>) {
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
      <HorizontalLayout theme={"spacing"}>
        <ShiftSelect
          theme={"small"}
          label={"Prvni smena"}
          selectedShift={props.request.firstShift}
          excludedShifts={[props.request.forbiddenFollowup]}
          onSelect={e => handleUpdate({
            firstShift: e
          })}/>
        <ShiftSelect
          theme={"small"}
          label={"Nasledujici smena"}
          selectedShift={props.request.forbiddenFollowup}
          excludedShifts={[props.request.firstShift]}
          onSelect={e => handleUpdate({
            forbiddenFollowup: e
          })}/>
        <NumberField
          theme={"small"}
          style={{ width: "50px" }}
          label={"Pokuta"}
          value={props.request.penalty.toString()}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          onChange={e => handleUpdate({ penalty: Number.parseInt(e.target.value)})}
        />
      </HorizontalLayout>
    </Card>
  );
}
