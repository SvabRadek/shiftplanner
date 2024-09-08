import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import { useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftFollowupRestrictionRequestDTO";
import { ShiftSelect } from "Frontend/components/ShiftSelect";
import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { Icon } from "@hilla/react-components/Icon";
import { Button } from "@hilla/react-components/Button";
import { CardFooter } from "Frontend/components/CardFooter";
import "@vaadin/icons";

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
    <Card style={{ width: "100%" }}>
      <HorizontalLayout theme={"spacing"}>
        <ShiftSelect
          theme={"small"}
          label={"Prvni smena"}
          selectedShift={props.request.firstShift}
          onSelect={e => handleUpdate({
            firstShift: e
          })}/>
        <ShiftSelect
          theme={"small"}
          label={"Nasledujici smena"}
          selectedShift={props.request.forbiddenFollowup}
          onSelect={e => handleUpdate({
            forbiddenFollowup: e
          })}/>
        <NumberField
          theme={"small"}
          style={{ width: "50px" }}
          label={"Pokuta"}
          value={props.request.penalty.toString()}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          onChange={e => handleUpdate({ penalty: Number.parseInt(e.target.value) })}
        />
      </HorizontalLayout>
      <CardFooter style={{ paddingTop: 0 }}>
        <Button onClick={() => props.onAction({ type: CRUDActions.DELETE, payload: props.request })}
                disabled={modeCtx.mode !== ScheduleMode.EDIT}
                theme={"small icon"}>
          <Icon icon={"vaadin:trash"}/>
        </Button>
      </CardFooter>
    </Card>
  );
}
