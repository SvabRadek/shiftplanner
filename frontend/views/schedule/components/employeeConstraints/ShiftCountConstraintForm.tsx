import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import { Button } from "@hilla/react-components/Button";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { Tooltip } from "@hilla/react-components/Tooltip";
import { Card } from "Frontend/components/Card";
import WorkerId from "Frontend/generated/com/cocroachden/planner/lib/WorkerId";
import { ShiftSelect } from "Frontend/components/ShiftSelect";

type Props = {
  request: ShiftsPerScheduleRequestDTO
  excludedShifts: WorkShifts[]
  readonly?: boolean
  onChange?: (previous: WorkShifts, newValue: ShiftsPerScheduleRequestDTO) => void
  onRemove?: (owner: WorkerId, shift: WorkShifts) => void
}

export function ShiftCountConstraintForm(props: Props) {

  function handleRemove() {
    props.onRemove?.(props.request.owner, props.request.targetShift);
  }

  function handleUpdate(partial: Partial<ShiftsPerScheduleRequestDTO>) {
    props.onChange?.(
      props.request.targetShift,
      { ...props.request, ...partial }
    )
  }

  return (
    <Card style={{marginTop: "5px", marginBottom: "5px"}}>
      <HorizontalLayout theme={"spacing"} style={{
        justifyContent: "end",
        width: "100%",
        marginBottom: "10px"
      }}>
        <Button theme={"small"} onClick={handleRemove} disabled={props.readonly}>
          Odstranit
        </Button>
      </HorizontalLayout>
      <HorizontalLayout style={{ width: "100%" }} theme={"spacing"}>
        <ShiftSelect
          theme={"small"}
          label={"Smena"}
          selectedShift={props.request.targetShift}
          onSelect={e => handleUpdate({ targetShift: e })}
        />
        <NumberField
          style={{ width: "100px" }}
          theme={"align-center small"}
          label={"Pocet"}
          value={props.request.softMin.toString()}
          stepButtonsVisible
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
          style={{ width: "100px" }}
          theme={"align-center small"}
          label={"Odchylka"}
          stepButtonsVisible
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
          style={{ width: "100px" }}
          label={"Pokuta"}
          theme={"align-center small"}
          stepButtonsVisible
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
    </Card>
  );
}


