import { Dialog } from "@hilla/react-components/Dialog";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { TextField } from "@hilla/react-components/TextField";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import {
  ShiftPerScheduleConstraintForm
} from "Frontend/views/schedule/components/employeesettings/constraintform/ShiftPerScheduleConstraintForm";
import { Icon } from "@hilla/react-components/Icon";
import { CrudAction, CRUDActions, generateUUID } from "Frontend/util/utils";
import { Card } from "Frontend/components/Card";
import { defaultConstraints } from "Frontend/views/schedule/DefaultEmptyConstraints";
import {
  ShiftPatternConstraintForm
} from "Frontend/views/schedule/components/employeesettings/constraintform/ShiftPatternConstraintForm";
import {
  TripleShiftConstraintForm
} from "Frontend/views/schedule/components/employeesettings/constraintform/TripleShiftConstraintForm";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";
import { NumberField } from "@hilla/react-components/NumberField";
import {
  TeamAssignmentConstraintForm
} from "Frontend/views/schedule/components/employeesettings/constraintform/TeamAssignmentConstraintForm";
import {
  WeekendConstraintForm
} from "Frontend/views/schedule/components/employeesettings/constraintform/WeekendConstraintForm";
import {
  EvenDistributionConstraintForm
} from "Frontend/views/schedule/components/employeesettings/constraintform/EvenDistributionConstraintForm";
import "@vaadin/icons";
import EmployeeAssignmentDTO
  from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import ShiftsPerScheduleConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleConstraintDTO";
import ShiftPatternConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ShiftPatternConstraintDTO";
import TripleShiftConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/TripleShiftConstraintDTO";
import TeamAssignmentConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/TeamAssignmentConstraintDTO";
import WeekendConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/WeekendConstraintDTO";
import EvenShiftDistributionConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EvenShiftDistributionConstraintDTO";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";

type Props = {
  assignment: EmployeeAssignmentDTO
  employee: EmployeeDTO
  isOpen: boolean
  onOpenChanged: (value: boolean) => void
  shiftsPerScheduleRequests: ShiftsPerScheduleConstraintDTO[]
  onShiftPerScheduleAction: (action: CrudAction<ShiftsPerScheduleConstraintDTO>) => void
  shiftPatternRequests: ShiftPatternConstraintDTO[],
  onShiftPatternRequestsAction: (action: CrudAction<ShiftPatternConstraintDTO>) => void
  tripleShiftConstraintRequest: TripleShiftConstraintDTO[]
  onTripleShiftConstraintAction: (action: CrudAction<TripleShiftConstraintDTO>) => void
  teamAssignmentRequests: TeamAssignmentConstraintDTO[],
  onTeamAssignmentRequestAction: (action: CrudAction<TeamAssignmentConstraintDTO>) => void
  weekendRequests: WeekendConstraintDTO[],
  onWeekendRequestRequestAction: (action: CrudAction<WeekendConstraintDTO>) => void
  evenDistributionRequests: EvenShiftDistributionConstraintDTO[]
  onEvenDistributionRequestsAction: (action: CrudAction<EvenShiftDistributionConstraintDTO>) => void
  onAssignmentAction: (action: CrudAction<EmployeeAssignmentDTO>) => void
  readonly?: boolean
}

export function EmployeeConstraintsDialog(props: Props) {

  if (!props.assignment) return null

  function handleUpdateAssignment(value: Partial<EmployeeAssignmentDTO>) {
    props.onAssignmentAction({
      type: CRUDActions.UPDATE,
      payload: {
        ...props.assignment!,
        ...value
      }
    })
  }

  function handleCreateNewShiftPerSchedule() {
    props.onShiftPerScheduleAction({
      type: CRUDActions.CREATE,
      payload: generateNewUniqueShiftsPerSchedule(
        props.assignment!.employeeId,
        props.shiftsPerScheduleRequests.map(r => r.targetShift)
      )
    });
  }

  function handleCreateNewShiftPattern() {
    props.onShiftPatternRequestsAction({
        type: CRUDActions.CREATE,
        payload: generateNewShiftPattern(props.assignment?.employeeId!)
      }
    )
  }

  function handleCreateNewTeamAssignment() {
    props.onTeamAssignmentRequestAction({
        type: CRUDActions.CREATE,
        payload: generateNewTeamAssignment(props.assignment?.employeeId!)
      }
    )
  }

  function handleCreateNewWeekendRequest() {
    props.onWeekendRequestRequestAction({
        type: CRUDActions.CREATE,
        payload: generateNewWeekendRequest(props.assignment?.employeeId!)
      }
    )
  }

  function handleCreateNewEvenDistributionRequest() {
    props.onEvenDistributionRequestsAction({
        type: CRUDActions.CREATE,
        payload: generateNewEvenDistributionRequest(props.assignment?.employeeId!)
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
      payload: generateNewTripleShiftConstraintRequest(props.assignment?.employeeId!)
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
            <TextField label={"Jméno"} value={props.employee.firstName} readonly/>
            <TextField label={"Příjmení"} value={props.employee.lastName} readonly/>
            <NumberField
              label={"Váha"}
              value={props.assignment.weight.toString()}
              style={{ width: "100px" }}
              stepButtonsVisible
              readonly={props.readonly}
              onChange={e => handleUpdateAssignment({
                weight: Number.parseInt(e.target.value)
              })}
            />
          </HorizontalLayout>
        </Card>
        {renderSectionHeader("Pocet smen na rozvrh", "vaadin:plus", handleCreateNewShiftPerSchedule)}
        {props.shiftsPerScheduleRequests.map(request => (
          <ShiftPerScheduleConstraintForm
            key={request.owner + request.targetShift}
            request={request}
            onShiftCountAction={props.onShiftPerScheduleAction}
            excludedShifts={props.shiftsPerScheduleRequests.map(r => r.targetShift)}
            readonly={props.readonly}
          />
        ))}
        {renderSectionHeader("Schéma směn", "vaadin:plus", handleCreateNewShiftPattern, props.shiftPatternRequests.length > 0)}
        {props.shiftPatternRequests.map(request => (
          <ShiftPatternConstraintForm
            key={request.id}
            request={request}
            onAction={props.onShiftPatternRequestsAction}
          />
        ))}
        {renderSectionHeader("Přirazení do týmu", "vaadin:plus", handleCreateNewTeamAssignment)}
        {props.teamAssignmentRequests.map(request => (
          <TeamAssignmentConstraintForm key={request.id} request={request} onAction={props.onTeamAssignmentRequestAction}/>
        ))}
        {renderSectionHeader("Nastavení trojitých směn", "vaadin:plus", handleCreateNewTripleShiftConstraint)}
        {props.tripleShiftConstraintRequest.map(request => (
          <TripleShiftConstraintForm key={request.id} request={request} onAction={props.onTripleShiftConstraintAction}/>
        ))}
        {renderSectionHeader("Nastaveni víkendů", "vaadin:plus", handleCreateNewWeekendRequest)}
        {props.weekendRequests.map(request => (
          <WeekendConstraintForm key={request.id} request={request} onAction={props.onWeekendRequestRequestAction}/>
        ))}
        {renderSectionHeader("Nastaveni rozložení směn", "vaadin:plus", handleCreateNewEvenDistributionRequest)}
        {props.evenDistributionRequests.map(request => (
            <EvenDistributionConstraintForm key={request.id} request={request} onAction={props.onEvenDistributionRequestsAction}/>
        ))}
        <HorizontalLayout style={{ width: "100%", justifyContent: "end" }}>
          <Button onClick={() => props.onOpenChanged(false)}>Zavřít</Button>
        </HorizontalLayout>
      </VerticalLayout>
    </Dialog>
  );
}

function generateNewUniqueShiftsPerSchedule(employeeId: string, excludeShifts: WorkShifts[]): ShiftsPerScheduleConstraintDTO {
  const allowedShifts = Object.values(WorkShifts).filter(val => !excludeShifts.some(s => s === val))
  return {
    ...defaultConstraints["SHIFTS_PER_SCHEDULE"].constraint,
    targetShift: allowedShifts[0],
    owner: employeeId,
    id: generateUUID()
  }
}

function generateNewShiftPattern(employeeId: string): ShiftPatternConstraintDTO {
  return {
    ...defaultConstraints["SHIFT_PATTERN_CONSTRAINT"].constraint,
    owner: employeeId,
    id: generateUUID()
  }
}

function generateNewTeamAssignment(employeeId: string): TeamAssignmentConstraintDTO {
  return {
    ...defaultConstraints["TEAM_ASSIGNMENT"].constraint,
    owner: employeeId,
    id: generateUUID()
  }
}

function generateNewWeekendRequest(employeeId: string): WeekendConstraintDTO {
  return {
    ...defaultConstraints["WEEKEND_CONSTRAINT"].constraint,
    owner: employeeId,
    id: generateUUID()
  }
}

function generateNewEvenDistributionRequest(employeeId: string): EvenShiftDistributionConstraintDTO {
  return {
    ...defaultConstraints["EVEN_SHIFT_DISTRIBUTION"].constraint,
    owner: employeeId,
    id: generateUUID()
  }
}

function generateNewTripleShiftConstraintRequest(employeeId: string): TripleShiftConstraintDTO {
  return {
    ...defaultConstraints["TRIPLE_SHIFTS_CONSTRAINT"].constraint,
    owner: employeeId
  }
}
