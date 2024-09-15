import {CrudAction, CRUDActions} from "Frontend/util/utils";
import {Card} from "Frontend/components/Card";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {NumberField} from "@hilla/react-components/NumberField";
import {ScheduleMode, ScheduleModeCtx} from "Frontend/views/schedule/ScheduleModeCtxProvider";
import {useContext} from "react";
import {Icon} from "@hilla/react-components/Icon";
import {Button} from "@hilla/react-components/Button";
import {Checkbox} from "@hilla/react-components/Checkbox";
import "@vaadin/icons";
import TripleShiftConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/TripleShiftConstraintDTO";

type Props = {
  request: TripleShiftConstraintDTO,
  onAction: (action: CrudAction<TripleShiftConstraintDTO>) => void
}

export function TripleShiftConstraintForm(props: Props) {

  const { mode } = useContext(ScheduleModeCtx);

  function handleUpdate(update: Partial<TripleShiftConstraintDTO>) {
    props.onAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.request,
        ...update
      }
    })
  }

  function handleRemove() {
    props.onAction({
      type: CRUDActions.DELETE,
      payload: props.request
    })
  }

  return (
    <Card style={{ width: "100%" }}>
      <HorizontalLayout style={{ width: "100%" }}>
        <HorizontalLayout style={{ width: "100%" }}>
          <Checkbox
            label={"Jsou povoleny"}
            checked={props.request.areAllowed}
            onCheckedChanged={e => props.onAction({
              type: CRUDActions.UPDATE,
              payload: {
                ...props.request,
                areAllowed: e.detail.value
              }
            })}
          >

          </Checkbox>
          <NumberField
            label={"Pokuta za trojsmenu mimo vikend"}
            style={{ width: 250 }}
            readonly={mode !== ScheduleMode.EDIT}
            theme={"small"}
            value={props.request.penaltyForShiftTripletOutsideWeekend.toString()}
            onChange={e => handleUpdate({
              penaltyForShiftTripletOutsideWeekend: Number.parseInt(e.target.value)
            })}
          />
        </HorizontalLayout>
        <Button theme={"small icon"}
                style={{ alignSelf: "start" }}
                onClick={handleRemove}
                disabled={mode !== ScheduleMode.EDIT}>
          <Icon icon={"vaadin:trash"}></Icon>
        </Button>
      </HorizontalLayout>
    </Card>
  );
}
