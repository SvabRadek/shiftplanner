import ShiftPatternRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftPatternRequestDTO";
import { arePatternsSame, CrudAction, CRUDActions } from "Frontend/util/utils";
import { Card } from "Frontend/components/Card";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { NumberField } from "@hilla/react-components/NumberField";
import { useContext } from "react";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { Select, SelectItem } from "@hilla/react-components/Select";
import { GridCell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import { apolinarPattern, classicPattern } from "Frontend/views/schedule/DefaultEmptyConstraints";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";

type Props = {
  request: ShiftPatternRequestDTO
  onAction: (action: CrudAction<ShiftPatternRequestDTO>) => void
}

const selectItems: SelectItem[] = [
  {
    label: "Apolinar",
    value: "apolinar"
  },
  {
    label: "Klasicky",
    value: "classic"
  },
  {
    label: "Libovolny",
    value: "custom"
  }
]

export function ShiftPatternConstraintForm(props: Props) {

  const { mode } = useContext(ScheduleModeCtx);

  const selectedItem = arePatternsSame(props.request.shiftPattern, apolinarPattern)
    ? selectItems[0]
    : arePatternsSame(props.request.shiftPattern, classicPattern)
      ? selectItems[1]
      : selectItems[2]

  function handleRemove() {
    props.onAction({
      type: CRUDActions.DELETE,
      payload: props.request
    })
  }

  function handleUpdate(value: Partial<ShiftPatternRequestDTO>) {
    props.onAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.request,
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
        ...props.request,
        shiftPattern: props.request.shiftPattern.map((w, index) => {
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
        ...props.request,
        shiftPattern: [...props.request.shiftPattern, WorkShifts.ANY]
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
            readonly={mode !== ScheduleMode.EDIT}
            theme={"small"}
            items={selectItems}
            value={selectedItem.value}
            onChange={e => handlePatternChange(e.target.value)}
          />
          <NumberField
            label={"Prvni den"}
            stepButtonsVisible
            style={{ width: 100 }}
            readonly={mode !== ScheduleMode.EDIT}
            theme={"small"}
            min={0}
            max={props.request.shiftPattern.length}
            value={props.request.startDayIndex.toString()}
            onChange={e => handleUpdate({
              startDayIndex: Number.parseInt(e.target.value)
            })}
          />
          <NumberField
            label={"Odmena"}
            readonly={mode !== ScheduleMode.EDIT}
            theme={"small"}
            style={{ width: 75 }}
            value={props.request.reward.toString()}
            onChange={e => handleUpdate({
              reward: Number.parseInt(e.target.value)
            })}
          />
        </HorizontalLayout>
        <Button onClick={handleRemove} theme={"small icon"}
                disabled={mode !== ScheduleMode.EDIT}>
          <Icon icon={"vaadin:trash"}/>
        </Button>
      </HorizontalLayout>
      <HorizontalLayout style={{ width: "100%", alignItems: "center", overflowX: "scroll" }}>
        {props.request.shiftPattern.map((shift, index) =>
          <GridCell key={index}
                    cell={{
                      shift: shift,
                      index: index,
                      owner: props.request.owner!,
                      date: new Date(0, 0, 0),
                      requestId: props.request.id,
                      isHighlighted: index === props.request.startDayIndex,
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
