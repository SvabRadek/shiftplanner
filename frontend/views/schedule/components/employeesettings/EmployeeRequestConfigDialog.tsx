import { Dialog } from "@hilla/react-components/Dialog";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { TextField } from "@hilla/react-components/TextField";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftsPerScheduleRequestDTO";
import {
  ShiftPerScheduleConstraintForm
} from "Frontend/views/schedule/components/employeesettings/constraintform/ShiftPerScheduleConstraintForm";
import { Icon } from "@hilla/react-components/Icon";
import { CrudAction, CRUDActions, generateUUID } from "Frontend/util/utils";
import { Card } from "Frontend/components/Card";
import { defaultConstraints } from "Frontend/views/schedule/DefaultEmptyConstraints";
import ShiftPatternRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftPatternRequestDTO";
import {
  ShiftPatternConstraintForm
} from "Frontend/views/schedule/components/employeesettings/constraintform/ShiftPatternConstraintForm";
import TripleShiftConstraintRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/TripleShiftConstraintRequestDTO";
import {
  TripleShiftConstraintForm
} from "Frontend/views/schedule/components/employeesettings/constraintform/TripleShiftConstraintForm";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/repository/EmployeeRecord";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";
import EmployeeId from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeId";

type Props = {
  employee?: EmployeeRecord
  isOpen: boolean
  onOpenChanged: (value: boolean) => void
  shiftsPerScheduleRequests: ShiftsPerScheduleRequestDTO[]
  onShiftPerScheduleAction: (action: CrudAction<ShiftsPerScheduleRequestDTO>) => void
  shiftPatternRequests: ShiftPatternRequestDTO[],
  onShiftPatternRequestsAction: (action: CrudAction<ShiftPatternRequestDTO>) => void
  tripleShiftConstraintRequest: TripleShiftConstraintRequestDTO[]
  onTripleShiftConstraintAction: (action: CrudAction<TripleShiftConstraintRequestDTO>) => void
  readonly?: boolean
}

export function EmployeeRequestConfigDialog(props: Props) {

  if (!props.employee) return null

  function handleCreateNewShiftPerSchedule() {
    props.onShiftPerScheduleAction({
      type: CRUDActions.CREATE,
      payload: generateNewUniqueShiftsPerSchedule(
        props.employee!,
        props.shiftsPerScheduleRequests.map(r => r.targetShift)
      )
    });
  }

  function handleCreateNewShiftPattern() {
    props.onShiftPatternRequestsAction({
        type: CRUDActions.CREATE,
        payload: generateNewShiftPattern(props.employee!)
      }
    )
  }

  function renderSectionHeader(
    title: string,
    icon: string,
    onClickCallback: () => void,
    disabled?: boolean
  ) {
    return (
      <HorizontalLayout theme={"spacing"} style={{ alignItems: "baseline" }}>
        <Button
          theme={"icon"}
          style={{ marginTop: "var(--lumo-size-xs)" }}
          disabled={props.readonly || disabled}
          onClick={onClickCallback}>
          <Icon icon={icon}/>
        </Button>
        <h6>{title}</h6>
      </HorizontalLayout>
    )
  }

  function handleCreateNewTripleShiftConstraint() {
    props.onTripleShiftConstraintAction({
      type: CRUDActions.CREATE,
      payload: generateNewTripleShiftConstraintRequest(props.employee!)
    })
  }

  return (
    <Dialog
      header-title={"Konfigurace zamestance"}
      opened={props.isOpen}
      draggable
      onOpenedChanged={e => {
        props.onOpenChanged(e.detail.value)
      }}
    >
      <VerticalLayout theme={"spacing"} style={{
        width: "75vw",
        maxWidth: "800px",
        maxHeight: "75vh"
      }}>
        <Card style={{ width: "100%" }}>
          <HorizontalLayout theme={"spacing"}>
            <TextField value={props.employee.firstName} readonly/>
            <TextField value={props.employee.lastName} readonly/>
          </HorizontalLayout>
        </Card>
        {renderSectionHeader("Pocet smen na rozvrh", "vaadin:plus", handleCreateNewShiftPerSchedule)}
        {props.shiftsPerScheduleRequests.map(request => (
          <ShiftPerScheduleConstraintForm
            key={request.owner.id + request.targetShift}
            request={request}
            onShiftCountAction={props.onShiftPerScheduleAction}
            excludedShifts={props.shiftsPerScheduleRequests.map(r => r.targetShift)}
            readonly={props.readonly}
          />
        ))}
        {renderSectionHeader("Schema smen", "vaadin:plus", handleCreateNewShiftPattern, props.shiftPatternRequests.length > 0)}
        {props.shiftPatternRequests.map(request => (
          <ShiftPatternConstraintForm
            key={request.id}
            request={request}
            onAction={props.onShiftPatternRequestsAction}
          />
        ))}
        {renderSectionHeader("Nastaveni trojitych smen", "vaadin:plus", handleCreateNewTripleShiftConstraint)}
        {props.tripleShiftConstraintRequest.map(request => (
          <TripleShiftConstraintForm key={request.id} request={request} onAction={props.onTripleShiftConstraintAction}/>
        ))}
        <HorizontalLayout style={{ width: "100%", justifyContent: "end" }}>
          <Button onClick={() => props.onOpenChanged(false)}>Zavrit</Button>
        </HorizontalLayout>
      </VerticalLayout>
    </Dialog>
  );
}

function generateNewUniqueShiftsPerSchedule(workerId: EmployeeId, excludeShifts: WorkShifts[]): ShiftsPerScheduleRequestDTO {
  const allowedShifts = Object.values(WorkShifts).filter(val => !excludeShifts.some(s => s === val))
  return {
    ...defaultConstraints.SHIFT_PER_SCHEDULE.constraint as unknown as ShiftsPerScheduleRequestDTO,
    targetShift: allowedShifts[0],
    owner: workerId,
    id: generateUUID()
  }
}

function generateNewShiftPattern(workerId: EmployeeId): ShiftPatternRequestDTO {
  return {
    ...defaultConstraints.SHIFT_PATTERN_CONSTRAINT.constraint as unknown as ShiftPatternRequestDTO,
    owner: workerId,
    id: generateUUID()
  }
}

function generateNewTripleShiftConstraintRequest(workerId: EmployeeId): TripleShiftConstraintRequestDTO {
  return {
    ...defaultConstraints.TRIPLE_SHIFTS_CONSTRAINT.constraint as unknown as TripleShiftConstraintRequestDTO,
    owner: { id: workerId.id }
  }
}
