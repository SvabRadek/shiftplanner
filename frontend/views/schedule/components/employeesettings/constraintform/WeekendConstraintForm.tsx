import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { useContext } from "react";
import { Icon } from "@hilla/react-components/Icon";
import { Button } from "@hilla/react-components/Button";
import { Checkbox } from "@hilla/react-components/Checkbox";
import "@vaadin/icons";
import WeekendConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/WeekendConstraintDTO";

type Props = {
  request: WeekendConstraintDTO,
  onAction: (action: CrudAction<WeekendConstraintDTO>) => void
}

export function WeekendConstraintForm(props: Props) {

  const { mode } = useContext(ScheduleModeCtx);

  function handleUpdate(update: Partial<WeekendConstraintDTO>) {
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
        <HorizontalLayout theme={"spacing"} style={{ width: "100%", alignItems: "baseline" }}>
          <Checkbox
            label={"Přiřazovat pouze plně pracovní víkendy"}
            checked={props.request.assignOnlyFullWorkingWeekends}
            onCheckedChanged={e => props.onAction({
              type: CRUDActions.UPDATE,
              payload: {
                ...props.request,
                assignOnlyFullWorkingWeekends: e.detail.value
              }
            })}
          />
          <NumberField
            label={"Pokuta za ne plně pracovní víkend"}
            style={{ width: 250 }}
            readonly={mode !== ScheduleMode.EDIT && props.request.assignOnlyFullWorkingWeekends}
            theme={"small"}
            value={props.request.penaltyForNotFullWorkingWeekend.toString()}
            onChange={e => handleUpdate({
              penaltyForNotFullWorkingWeekend: Number.parseInt(e.target.value)
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
