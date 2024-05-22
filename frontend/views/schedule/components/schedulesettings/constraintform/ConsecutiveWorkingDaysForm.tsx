import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ConsecutiveWorkingDaysRequestDTO";
import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import { useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { CardFooter } from "Frontend/components/CardFooter";
import { ShiftSelect } from "Frontend/components/ShiftSelect";

type Props = {
  request: ConsecutiveWorkingDaysRequestDTO
  onAction: (action: CrudAction<ConsecutiveWorkingDaysRequestDTO>) => void
}

export function ConsecutiveWorkingDaysForm(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx);

  function handleUpdate(value: Partial<ConsecutiveWorkingDaysRequestDTO>) {
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
        <ShiftSelect label={"Smena"}
                     theme={"small"}
                     selectedShift={props.request.targetShift}
                     onSelect={e => handleUpdate({ targetShift: e })}/>
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
