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
  excludedShifts: WorkShifts[]
  onChange?: (previous: WorkShifts, newValue: ShiftsPerScheduleRequestDTO) => void
  onRemove?: (owner: WorkerId, shift: WorkShifts) => void
}

export function ShiftCountConstraintForm(props: Props) {

  const [isAdvancedMode, setIsAdvancedMode] = useState<boolean>(false)

  const selectItems: SelectItem[] =
    Object.values(workShiftBindings)
      .map(binding => ({
        label: binding.fullText,
        value: binding.shift,
        disabled: props.request.targetShift !== binding.shift && props.excludedShifts.some(s => s === binding.shift)
      }))

  function handleRemove() {
    props.onRemove?.(props.request.owner, props.request.targetShift);
  }

  function handleUpdate(partial: Partial<ShiftsPerScheduleRequestDTO>) {
    props.onChange?.(
      props.request.targetShift,
      { ...props.request, ...partial }
    )
  }

  function renderWorkshiftSelect() {
    return (
      <Select
        theme={"small"}
        label={"Smena"}
        style={{ width: "200px" }}
        items={selectItems}
        value={props.request.targetShift}
        onChange={(e) => handleUpdate({ targetShift: e.target.value as WorkShifts })}
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
            handleUpdate({
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
            handleUpdate({
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
            handleUpdate({
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
              handleUpdate({
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
              handleUpdate({
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
              handleUpdate({
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
              handleUpdate({
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
              handleUpdate({
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
              handleUpdate({
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


