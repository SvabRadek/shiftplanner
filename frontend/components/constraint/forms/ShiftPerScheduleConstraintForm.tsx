import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import { Button } from "@hilla/react-components/Button";
import { Tooltip } from "@hilla/react-components/Tooltip";
import { Card } from "Frontend/components/Card";
import { ShiftSelect } from "Frontend/components/ShiftSelect";
import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { Icon } from "@hilla/react-components/Icon";
import "@vaadin/icons";
import ShiftsPerScheduleConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleConstraintDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";

type Props = {
  constraint: ShiftsPerScheduleConstraintDTO
  excludedShifts: WorkShifts[]
  onConstraintAction: (action: CrudAction<ShiftsPerScheduleConstraintDTO>) => void
  readonly: boolean
}

export function ShiftPerScheduleConstraintForm(props: Props) {

  function handleRemove() {
    props.onConstraintAction({
      type: CRUDActions.DELETE,
      payload: props.constraint
    })
  }

  function handleUpdate(partial: Partial<ShiftsPerScheduleConstraintDTO>) {
    props.onConstraintAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.constraint,
        ...partial
      }
    })
  }

  return (
    <Card style={{ marginTop: 5, marginBottom: 5, width: "100%" }}>
      <HorizontalLayout style={{
        width: "100%",
        alignItems: "baseline",
        justifyContent: "space-between"
      }} theme={"spacing"}>
        <HorizontalLayout theme={"spacing"}>
          <ShiftSelect
            theme={"small"}
            label={"Smena"}
            selectedShift={props.constraint.targetShift}
            excludedShifts={props.excludedShifts}
            onSelect={e => handleUpdate({ targetShift: e })}
            readonly={props.readonly}
          />
          <NumberField
            style={{ width: "75px" }}
            theme={"align-center small"}
            label={"Pocet"}
            value={props.constraint.softMin.toString()}
            disabled={props.readonly}
            onChange={e => {
              const newOptimum = Number.parseInt(e.target.value)
              const currentDeviation = props.constraint.hardMax - props.constraint.softMax
              handleUpdate({
                softMin: newOptimum,
                softMax: newOptimum,
                hardMin: newOptimum - currentDeviation,
                hardMax: newOptimum + currentDeviation
              })
            }}
            validate={() => props.constraint.softMin >= props.constraint.hardMin && props.constraint.softMax <= props.constraint.hardMax}
          >
            <Tooltip slot={"tooltip"} text={"Optimalni pocet smen prirazenych v ramci rozvrhu"}/>
          </NumberField>
          <NumberField
            style={{ width: "75px" }}
            theme={"align-center small"}
            label={"Odchylka"}
            onClick={() => handleUpdate({
              hardMax: Number.NaN,
              hardMin: Number.NaN
            })}
            disabled={props.readonly}
            value={(props.constraint.hardMax - props.constraint.softMax).toString()}
            onChange={e => {
              const newDeviation = Number.parseInt(e.target.value);
              handleUpdate({
                hardMax: props.constraint.softMax + newDeviation,
                hardMin: props.constraint.softMin - newDeviation
              })
            }}
          >
            <Tooltip slot={"tooltip"} text={"Povolena odchylka od optimalniho poctu smen"}/>
          </NumberField>
          <NumberField
            style={{ width: "50px" }}
            label={"Pokuta"}
            theme={"align-center small"}
            disabled={props.readonly}
            value={props.constraint.maxPenalty.toString()}
            onChange={e => {
              const newPenalty = Number.parseInt(e.target.value);
              handleUpdate({
                maxPenalty: newPenalty,
                minPenalty: newPenalty
              })
            }}
          >
            <Tooltip slot={"tooltip"} text={"Pokuta za prekroceni mekkych limitu"}/>
          </NumberField>
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


