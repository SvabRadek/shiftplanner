import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { useContext } from "react";
import { Icon } from "@hilla/react-components/Icon";
import { Button } from "@hilla/react-components/Button";
import TeamAssignmentRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/TeamAssignmentRequestDTO";
import { Checkbox } from "@hilla/react-components/Checkbox";
import "@vaadin/icons";

type Props = {
  request: TeamAssignmentRequestDTO,
  onAction: (action: CrudAction<TeamAssignmentRequestDTO>) => void
}

export function TeamAssignmentConstraintForm(props: Props) {

  const { mode } = useContext(ScheduleModeCtx);

  function handleUpdate(update: Partial<TeamAssignmentRequestDTO>) {
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
        <HorizontalLayout style={{ width: "100%", alignItems: "baseline" }} theme={"spacing"}>
          <NumberField
            label={"Tym"}
            stepButtonsVisible
            style={{ width: 100 }}
            readonly={mode !== ScheduleMode.EDIT}
            theme={"small"}
            value={props.request.teamId.toString()}
            onChange={e => handleUpdate({
              teamId: Number.parseInt(e.target.value)
            })}
          />
          <NumberField
            label={"Pokuta za neprirazeni podle vedouciho"}
            style={{ width: 250 }}
            readonly={mode !== ScheduleMode.EDIT || props.request.isLeader}
            theme={"small"}
            value={props.request.penalty.toString()}
            onChange={e => handleUpdate({
              penalty: Number.parseInt(e.target.value)
            })}
          />
          <Checkbox
            label={"Je vedouci"}
            checked={props.request.isLeader}
            onCheckedChanged={e => handleUpdate({
              isLeader: e.detail.value
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
