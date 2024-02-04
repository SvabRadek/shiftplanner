import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ConsecutiveWorkingDaysRequestDTO";
import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";

type Props = {
  request: ConsecutiveWorkingDaysRequestDTO
  onChange: (value: ConsecutiveWorkingDaysRequestDTO) => void
}

export function ConsecutiveWorkingDaysForm(props: Props) {

  function handleUpdate(value: Partial<ConsecutiveWorkingDaysRequestDTO>) {
    props.onChange({
      ...props.request,
      ...value
    })
  }

  return (
    <Card>
      <h6>Nastaveni poctu po sobe jdoucich pracovnich smen</h6>
      <HorizontalLayout theme={"spacing"}>
        <NumberField
          label={"Min"}
          value={props.request.hardMin.toString()}
          style={{ width: "100px" }}
          onChange={e => handleUpdate({ hardMin: Number.parseInt(e.target.value) })}
        />
        <NumberField
          label={"Soft Min"}
          style={{ width: "100px" }}
          value={props.request.softMin.toString()}
          onChange={e => handleUpdate({ softMin: Number.parseInt(e.target.value) })}
        />
        <NumberField
          label={"Penalty"}
          style={{ width: "100px" }}
          value={props.request.minPenalty.toString()}
          onChange={e => handleUpdate({ minPenalty: Number.parseInt(e.target.value) })}
        />
        <NumberField
          label={"Soft Max"}
          style={{ width: "100px" }}
          value={props.request.softMax.toString()}
          onChange={e => handleUpdate({ softMax: Number.parseInt(e.target.value) })}
        />
        <NumberField
          label={"Hard Max"}
          style={{ width: "100px" }}
          value={props.request.hardMax.toString()}
          onChange={e => handleUpdate({ hardMax: Number.parseInt(e.target.value) })}
        />
        <NumberField
          label={"Penalty"}
          style={{ width: "100px" }}
          value={props.request.maxPenalty.toString()}
          onChange={e => handleUpdate({ maxPenalty: Number.parseInt(e.target.value) })}
        />
      </HorizontalLayout>
    </Card>
  );
}
