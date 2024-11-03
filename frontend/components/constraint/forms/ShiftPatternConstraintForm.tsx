import { arePatternsSame, CrudAction, CRUDActions } from "Frontend/util/utils";
import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { NumberField } from "@hilla/react-components/NumberField";
import { useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { Select, SelectItem } from "@hilla/react-components/Select";
import { GridCell } from "Frontend/components/solverconfigurationeditor/grid/schedulegrid/GridCell";
import { apolinarPattern, classicPattern } from "Frontend/views/schedule/DefaultEmptyConstraints";
import "@vaadin/icons";
import ShiftPatternConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ShiftPatternConstraintDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";

type Props = {
  constraint: ShiftPatternConstraintDTO
  onAction: (action: CrudAction<ShiftPatternConstraintDTO>) => void
  readonly : boolean
}

const selectItems: SelectItem[] = [
  {
    label: "Apolinář",
    value: "apolinar"
  },
  {
    label: "Klasický",
    value: "classic"
  },
  {
    label: "Libovolný",
    value: "custom"
  }
]

export function ShiftPatternConstraintForm(props: Props) {

  const selectedItem = arePatternsSame(props.constraint.shiftPattern, apolinarPattern)
    ? selectItems[0]
    : arePatternsSame(props.constraint.shiftPattern, classicPattern)
      ? selectItems[1]
      : selectItems[2]

  function handleRemove() {
    props.onAction({
      type: CRUDActions.DELETE,
      payload: props.constraint
    })
  }

  function handleUpdate(value: Partial<ShiftPatternConstraintDTO>) {
    props.onAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.constraint,
        ...value
      }
    })
  }

  function handleUpdateShifts(
    shift: WorkShifts,
    shiftArrayIndex: number
  ) {
    props.onAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.constraint,
        shiftPattern: props.constraint.shiftPattern.map((w, index) => {
          if (index !== shiftArrayIndex) return w
          return shift
        })
      }
    });
  }

  function handleAddShift() {
    props.onAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.constraint,
        shiftPattern: [...props.constraint.shiftPattern, WorkShifts.ANY]
      }
    })
  }

  function handlePatternChange(pattern: string) {
    if (pattern === "apolinar") {
      handleUpdate({
        shiftPattern: apolinarPattern
      })
    }
    if (pattern === "classic") {
      handleUpdate({
        shiftPattern: classicPattern
      })
    }
    if (pattern === "custom") {
      handleUpdate({
        shiftPattern: []
      })
    }
  }

  return (
    <Card style={{ width: "100%" }}>
      <HorizontalLayout theme={"spacing"} style={{ justifyContent: "space-between", width: "100%" }}>
        <HorizontalLayout theme={"spacing"} style={{ justifyContent: "start", alignItems: "baseline" }}>
          <Select
            label={"Typ"}
            readonly={props.readonly}
            theme={"small"}
            items={selectItems}
            value={selectedItem.value}
            onChange={e => handlePatternChange(e.target.value)}
          />
          <NumberField
            label={"Prvni den"}
            stepButtonsVisible
            style={{ width: 100 }}
            readonly={props.readonly}
            theme={"small"}
            min={0}
            max={props.constraint.shiftPattern.length}
            value={props.constraint.startDayIndex.toString()}
            onChange={e => handleUpdate({
              startDayIndex: Number.parseInt(e.target.value)
            })}
          />
          <NumberField
            label={"Odmena"}
            readonly={props.readonly}
            theme={"small"}
            style={{ width: 75 }}
            value={props.constraint.reward.toString()}
            onChange={e => handleUpdate({
              reward: Number.parseInt(e.target.value)
            })}
          />
        </HorizontalLayout>
        <Button onClick={handleRemove} theme={"small icon"}
                disabled={props.readonly}>
          <Icon icon={"vaadin:trash"}/>
        </Button>
      </HorizontalLayout>
      <HorizontalLayout style={{ width: "100%", alignItems: "center", overflowX: "scroll" }}>
        {props.constraint.shiftPattern.map((shift, index) =>
          <GridCell key={index}
                    cell={{
                      shift: shift,
                      index: index,
                      employeeId: props.constraint.owner,
                      date: new Date(0, 0, 0),
                      constraintId: props.constraint.id,
                      isHighlighted: index === props.constraint.startDayIndex,
                    }}
                    onShiftChange={cell => handleUpdateShifts(cell.shift, cell.index)}
          />
        )}
        <Button theme={"icon small"}
                onClick={handleAddShift}
                style={{ marginLeft: 1, width: 51, height: 52, borderRadius: 0 }}>
          <Icon icon={"vaadin:plus"}/>
        </Button>
      </HorizontalLayout>
    </Card>
  );
}
