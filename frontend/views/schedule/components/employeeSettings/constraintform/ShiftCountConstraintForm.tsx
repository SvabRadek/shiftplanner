import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import { Button } from "@hilla/react-components/Button";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { Tooltip } from "@hilla/react-components/Tooltip";
import { Card } from "Frontend/components/Card";
import { ShiftSelect } from "Frontend/components/ShiftSelect";
import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { Icon } from "@hilla/react-components/Icon";

type Props = {
  request: ShiftsPerScheduleRequestDTO
  excludedShifts: WorkShifts[]
  onShiftCountAction: (action: CrudAction<ShiftsPerScheduleRequestDTO>) => void
  readonly?: boolean
}

export function ShiftCountConstraintForm(props: Props) {

  function handleRemove() {
    props.onShiftCountAction({
      type: CRUDActions.DELETE,
      payload: props.request
    })
  }

  function handleUpdate(partial: Partial<ShiftsPerScheduleRequestDTO>) {
    props.onShiftCountAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.request,
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
            selectedShift={props.request.targetShift}
            excludedShifts={props.excludedShifts}
            onSelect={e => handleUpdate({ targetShift: e })}
          />
          <NumberField
            style={{ width: "75px" }}
            theme={"align-center small"}
            label={"Pocet"}
            value={props.request.softMin.toString()}
            disabled={props.readonly}
            onChange={e => {
              const newOptimum = Number.parseInt(e.target.value)
              const currentDeviation = props.request.hardMax - props.request.softMax
              handleUpdate({
                softMin: newOptimum,
                softMax: newOptimum,
                hardMin: newOptimum - currentDeviation,
                hardMax: newOptimum + currentDeviation
              })
            }}
            validate={() => props.request.softMin >= props.request.hardMin && props.request.softMax <= props.request.hardMax}
          >
            <Tooltip slot={"tooltip"} text={"Optimalni pocet smen prirazenych v ramci rozvrhu"}/>
          </NumberField>
          <NumberField
            style={{ width: "75px" }}
            theme={"align-center small"}
            label={"Odchylka"}
            disabled={props.readonly}
            value={(props.request.hardMax - props.request.softMax).toString()}
            onChange={e => {
              const newDeviation = Number.parseInt(e.target.value);
              handleUpdate({
                hardMax: props.request.softMax + newDeviation,
                hardMin: props.request.softMin - newDeviation
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
            value={props.request.maxPenalty.toString()}
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


