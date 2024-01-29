import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { useState } from "react";
import { Button } from "@hilla/react-components/Button";
import { Select, SelectItem } from "@hilla/react-components/Select";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { workShiftBindings } from "Frontend/views/schedule/WorkShiftBindigs";
import { Tooltip } from "@hilla/react-components/Tooltip";
import { Card } from "Frontend/components/Card";
import WorkerId from "Frontend/generated/com/cocroachden/planner/lib/WorkerId";

type Props = {
  request: ShiftsPerScheduleRequestDTO
  onChange?: (newValue: ShiftsPerScheduleRequestDTO) => void
  onRemove?: (owner: WorkerId, shift: WorkShifts) => void
}

const selectItems: SelectItem[] =
  Object.values(workShiftBindings)
    .map(binding => ({
      label: binding.fullText,
      value: binding.shift
    }))

export function ShiftCountConstraintForm(props: Props) {

  const [isAdvancedMode, setIsAdvancedMode] = useState<boolean>(false)

  function handleRemove() {
    props.onRemove?.(props.request.owner, props.request.targetShift);
  }

  function renderWorkshiftSelect() {
    return (
      <Select
        theme={"small"}
        label={"Smena"}
        style={{ width: "200px" }}
        items={selectItems}
        value={props.request.targetShift}
        onChange={(e) => {
          props.onChange?.({ ...props.request, targetShift: e.target.value as WorkShifts })
        }}
      >
      </Select>
    )
  }

  function renderBasicForm() {
    return (
      <HorizontalLayout theme={"spacing"}>
        {renderWorkshiftSelect()}
        <NumberField
          style={{ width: "100px" }}
          theme={"align-center small"}
          label={"Pocet"}
          value={props.request.softMin.toString()}
          stepButtonsVisible
          onChange={e => {
            props.onChange?.({
              ...props.request,
              softMin: Number.parseInt(e.target.value),
              softMax: Number.parseInt(e.target.value)
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
          value={(props.request.hardMax - props.request.softMax).toString()}
          onChange={e => {
            props.onChange?.({
              ...props.request,
              hardMax: props.request.softMax + Number.parseInt(e.target.value),
              hardMin: props.request.softMin - Number.parseInt(e.target.value)
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
          value={props.request.maxPenalty.toString()}
          onChange={e => {
            props.onChange?.({
              ...props.request,
              maxPenalty: Number.parseInt(e.target.value),
              minPenalty: Number.parseInt(e.target.value)
            })
          }}
        >
          <Tooltip slot={"tooltip"} text={"Pokuta za prekroceni mekkych limitu"}/>
        </NumberField>
      </HorizontalLayout>
    )
  }

  function renderAdvancedForm() {
    return (
      <VerticalLayout>
        {renderWorkshiftSelect()}
        <HorizontalLayout theme={"spacing"}>
          <NumberField
            theme={"align-center small"}
            stepButtonsVisible
            style={{ width: "100px" }}
            label={"Minimum"}
            value={props.request.hardMin.toString()}
            onChange={e => {
              props.onChange?.({
                ...props.request,
                hardMin: Number.parseInt(e.target.value)
              })
            }}
          />
          <NumberField
            theme={"align-center small"}
            stepButtonsVisible
            style={{ width: "100px" }}
            label={"Min Optimum"}
            value={props.request.softMin.toString()}
            onChange={e => {
              props.onChange?.({
                ...props.request,
                softMin: Number.parseInt(e.target.value)
              })
            }}
            validate={() => props.request.hardMin < props.request.softMin}
          />
          <NumberField
            theme={"align-center small"}
            stepButtonsVisible
            style={{ width: "100px" }}
            label={"Pokuta"}
            value={props.request.minPenalty.toString()}
            onChange={e => {
              props.onChange?.({
                ...props.request,
                minPenalty: Number.parseInt(e.target.value)
              })
            }}
          />
        </HorizontalLayout>
        <HorizontalLayout theme={"spacing"}>
          <NumberField
            label={"Maximum"}
            theme={"align-center small"}
            stepButtonsVisible
            style={{ width: "100px" }}
            value={props.request.hardMax.toString()}
            onChange={e => {
              props.onChange?.({
                ...props.request,
                hardMax: Number.parseInt(e.target.value)
              })
            }}
          />
          <NumberField
            label={"Max Optimum"}
            theme={"align-center small"}
            stepButtonsVisible
            style={{ width: "100px" }}
            value={props.request.softMax.toString()}
            onChange={e => {
              props.onChange?.({
                ...props.request,
                softMax: Number.parseInt(e.target.value)
              })
            }}
          />
          <NumberField
            label={"Pokuta"}
            theme={"align-center small"}
            stepButtonsVisible
            style={{ width: "100px" }}
            value={props.request.maxPenalty.toString()}
            onChange={e => {
              props.onChange?.({
                ...props.request,
                maxPenalty: Number.parseInt(e.target.value)
              })
            }}
          />
        </HorizontalLayout>
      </VerticalLayout>
    )
  }

  return (
    <Card
      style={{
        marginTop: "5px",
        marginBottom: "5px"
      }}
    >
      <HorizontalLayout
        theme={"spacing"}
        style={{
          justifyContent: "end",
          width: "100%",
          marginBottom: "10px"
        }}
      >
        <Button
          style={{ alignSelf: "end" }}
          theme={"small"}
          title={"zobrazeni"}
          onClick={() => setIsAdvancedMode(prevState => !prevState)}
        >
          {isAdvancedMode ? "Zakladni" : "Detailni"}
        </Button>
        <Button
          theme={"small"}
          onClick={handleRemove}
        >
          Odstranit
        </Button>
      </HorizontalLayout>
      {isAdvancedMode ? renderAdvancedForm() : renderBasicForm()}
    </Card>
  );
}


