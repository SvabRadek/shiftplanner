import {Card} from "Frontend/components/Card";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {NumberField} from "@hilla/react-components/NumberField";
import {ShiftSelect} from "Frontend/components/ShiftSelect";
import {CrudAction, CRUDActions} from "Frontend/util/utils";
import {Button} from "@hilla/react-components/Button";
import {Icon} from "@hilla/react-components/Icon";
import {CardFooter} from "Frontend/components/CardFooter";
import "@vaadin/icons";
import EmployeesPerShiftConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftConstraintDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";

type Props = {
  constraint: EmployeesPerShiftConstraintDTO
  excludedShifts: WorkShifts[]
  onAction: (action: CrudAction<EmployeesPerShiftConstraintDTO>) => void
  readonly: boolean
}

export function EmployeesPerShiftForm(props: Props) {

  function handleUpdate(value: Partial<EmployeesPerShiftConstraintDTO>) {
    props.onAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.constraint,
        ...value
      }
    })
  }

  return (
    <Card style={{ width: "100%" }}>
      <HorizontalLayout theme={"spacing"} style={{ alignItems: "center" }}>
        <ShiftSelect
          theme={"small"}
          label={"Smena"}
          selectedShift={props.constraint.targetShift}
          onSelect={e => handleUpdate({ targetShift: e })}
          readonly={props.readonly}
        />
        <NumberField
          theme={"small"}
          readonly={props.readonly}
          label={"Min"}
          value={props.constraint.hardMin.toString()}
          style={{ width: "50px" }}
          onChange={e => handleUpdate({ hardMin: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={props.readonly}
          label={"Soft Min"}
          style={{ width: "75px" }}
          value={props.constraint.softMin.toString()}
          onChange={e => handleUpdate({ softMin: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={props.readonly}
          label={"Pokuta"}
          style={{ width: "50px" }}
          value={props.constraint.minPenalty.toString()}
          onChange={e => handleUpdate({ minPenalty: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={props.readonly}
          label={"Soft Max"}
          style={{ width: "75px" }}
          value={props.constraint.softMax.toString()}
          onChange={e => handleUpdate({ softMax: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={props.readonly}
          label={"Max"}
          style={{ width: "50px" }}
          value={props.constraint.hardMax.toString()}
          onChange={e => handleUpdate({ hardMax: Number.parseInt(e.target.value) })}
        />
        <NumberField
          theme={"small"}
          readonly={props.readonly}
          label={"Pokuta"}
          style={{ width: "50px" }}
          value={props.constraint.maxPenalty.toString()}
          onChange={e => handleUpdate({ maxPenalty: Number.parseInt(e.target.value) })}
        />
      </HorizontalLayout>
      <CardFooter style={{ paddingTop: 0 }}>
        <Button onClick={() => props.onAction({ type: CRUDActions.DELETE, payload: props.constraint })}
                disabled={props.readonly}
                theme={"small icon"}>
          <Icon icon={"vaadin:trash"}/>
        </Button>
      </CardFooter>
    </Card>
  );
}
