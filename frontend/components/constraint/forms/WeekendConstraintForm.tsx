import {CrudAction, CRUDActions} from "Frontend/util/utils";
import {Card} from "Frontend/components/Card";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {NumberField} from "@hilla/react-components/NumberField";
import {Icon} from "@hilla/react-components/Icon";
import {Button} from "@hilla/react-components/Button";
import {Checkbox} from "@hilla/react-components/Checkbox";
import "@vaadin/icons";
import WeekendConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/WeekendConstraintDTO";

type Props = {
  constraint: WeekendConstraintDTO,
  onAction: (action: CrudAction<WeekendConstraintDTO>) => void
  readonly: boolean
}

export function WeekendConstraintForm(props: Props) {

  function handleUpdate(update: Partial<WeekendConstraintDTO>) {
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
        <HorizontalLayout theme={"spacing"} style={{ width: "100%", alignItems: "baseline" }}>
          <Checkbox
            label={"Přiřazovat pouze plně pracovní víkendy"}
            checked={props.constraint.assignOnlyFullWorkingWeekends}
            onCheckedChanged={e => props.onAction({
              type: CRUDActions.UPDATE,
              payload: {
                ...props.constraint,
                assignOnlyFullWorkingWeekends: e.detail.value
              }
            })}
          />
          <NumberField
            label={"Pokuta za ne plně pracovní víkend"}
            style={{ width: 250 }}
            readonly={props.readonly && props.constraint.assignOnlyFullWorkingWeekends}
            theme={"small"}
            value={props.constraint.penaltyForNotFullWorkingWeekend.toString()}
            onChange={e => handleUpdate({
              penaltyForNotFullWorkingWeekend: Number.parseInt(e.target.value)
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
