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
import TeamAssignmentConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/TeamAssignmentConstraintDTO";

type Props = {
  constraint: TeamAssignmentConstraintDTO,
  onAction: (action: CrudAction<TeamAssignmentConstraintDTO>) => void
  readonly : boolean
}

export function TeamAssignmentConstraintForm(props: Props) {

  function handleUpdate(update: Partial<TeamAssignmentConstraintDTO>) {
    props.onAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.constraint,
        ...update
      }
    })
  }

  function handleRemove() {
    props.onAction({
      type: CRUDActions.DELETE,
      payload: props.constraint
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
            readonly={props.readonly}
            theme={"small"}
            value={props.constraint.teamId.toString()}
            onChange={e => handleUpdate({
              teamId: Number.parseInt(e.target.value)
            })}
          />
          <NumberField
            label={"Pokuta za neprirazeni podle vedouciho"}
            style={{ width: 250 }}
            readonly={props.readonly || props.constraint.isLeader}
            theme={"small"}
            value={props.constraint.penalty.toString()}
            onChange={e => handleUpdate({
              penalty: Number.parseInt(e.target.value)
            })}
          />
          <Checkbox
            label={"Je vedouci"}
            checked={props.constraint.isLeader}
            onCheckedChanged={e => handleUpdate({
              isLeader: e.detail.value
            })}
          />
        </HorizontalLayout>
        <Button theme={"small icon"}
                style={{ alignSelf: "start" }}
                onClick={handleRemove}
                disabled={props.readonly}>
          <Icon icon={"vaadin:trash"}></Icon>
        </Button>
      </HorizontalLayout>
    </Card>
  );
}
