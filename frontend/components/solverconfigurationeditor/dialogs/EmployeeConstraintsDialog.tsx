import {Dialog} from "@hilla/react-components/Dialog";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {TextField} from "@hilla/react-components/TextField";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {Button} from "@hilla/react-components/Button";
import {
    ShiftPerScheduleConstraintForm
} from "Frontend/components/constraint/forms/ShiftPerScheduleConstraintForm";
import {CrudAction, CRUDActions, generateUUID} from "Frontend/util/utils";
import {Card} from "Frontend/components/Card";
import {defaultConstraints} from "Frontend/views/schedule/DefaultEmptyConstraints";
import {
    ShiftPatternConstraintForm
} from "Frontend/components/constraint/forms/ShiftPatternConstraintForm";
import {
    TripleShiftConstraintForm
} from "Frontend/components/constraint/forms/TripleShiftConstraintForm";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";
import {NumberField} from "@hilla/react-components/NumberField";
import {
    TeamAssignmentConstraintForm
} from "Frontend/components/constraint/forms/TeamAssignmentConstraintForm";
import {
    WeekendConstraintForm
} from "Frontend/components/constraint/forms/WeekendConstraintForm";
import {
    EvenDistributionConstraintForm
} from "Frontend/components/constraint/forms/EvenDistributionConstraintForm";
import "@vaadin/icons";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import ShiftsPerScheduleConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleConstraintDTO";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import {SortedConstraints} from "Frontend/views/schedule/ConstraintUtils";
import ConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintDTO";
import {ConstraintSection} from "Frontend/components/constraint/ConstraintSection";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintType";
import {
    ShiftFollowupRestrictionForm
} from "Frontend/components/constraint/forms/ShiftFollowupRestrictionForm";
import {
    ConsecutiveWorkingDaysForm
} from "Frontend/components/constraint/forms/ConsecutiveWorkingDaysForm";

type Props = {
    assignment: EmployeeAssignmentDTO
    employee: EmployeeDTO
    isOpen: boolean
    sortedConstraints: SortedConstraints
    onConstraintAction: (action: CrudAction<ConstraintDTO>) => void
    onOpenChanged: (value: boolean) => void
    onAssignmentAction: (action: CrudAction<EmployeeAssignmentDTO>) => void
    readonly: boolean
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
                <Card style={{width: "100%"}}>
                    <HorizontalLayout theme={"spacing"}>
                        <TextField label={"Jméno"} value={props.employee.firstName} readonly/>
                        <TextField label={"Příjmení"} value={props.employee.lastName} readonly/>
                        <NumberField
                            label={"Váha"}
                            value={props.assignment.weight.toString()}
                            style={{width: "100px"}}
                            stepButtonsVisible
                            onChange={e => handleUpdateAssignment({
                                weight: Number.parseInt(e.target.value)
                            })}
                        />
                    </HorizontalLayout>
                </Card>
                <ConstraintSection
                    title={"Pocet smen na rozvrh"}
                    constraintsOfType={props.sortedConstraints["SHIFTS_PER_SCHEDULE"]}
                    onConstraintAction={props.onConstraintAction}
                    defaultNewConstraint={() => {
                        const excludedShifts = props.sortedConstraints["SHIFTS_PER_SCHEDULE"].map(c => c.targetShift)
                        return generateNewUniqueShiftsPerSchedule(props.employee.id, excludedShifts)
                    }}
                    owner={props.employee.id}
                    renderer={constraint => (<ShiftPerScheduleConstraintForm
                        key={constraint.id}
                        constraint={constraint}
                        onConstraintAction={props.onConstraintAction}
                        excludedShifts={props.sortedConstraints["SHIFTS_PER_SCHEDULE"].map(c => c.targetShift)}
                        readonly={props.readonly}
                    />)}
                />
                <ConstraintSection
                    title={"Schéma směn"}
                    constraintsOfType={props.sortedConstraints["SHIFT_PATTERN_CONSTRAINT"]}
                    onConstraintAction={props.onConstraintAction}
                    defaultNewConstraint={() => generateNewConstraint(props.employee.id, ConstraintType.SHIFT_PATTERN_CONSTRAINT)}
                    renderer={(constraint) => (<ShiftPatternConstraintForm
                        key={constraint.id}
                        constraint={constraint}
                        onAction={props.onConstraintAction}
                        readonly={props.readonly}
                    />)}
                />
                <ConstraintSection
                    title={"Přirazení do týmu"}
                    constraintsOfType={props.sortedConstraints["TEAM_ASSIGNMENT"]}
                    onConstraintAction={props.onConstraintAction}
                    defaultNewConstraint={() => generateNewConstraint(props.employee.id, ConstraintType.TEAM_ASSIGNMENT)}
                    renderer={(constraint) => (<TeamAssignmentConstraintForm
                        key={constraint.id}
                        constraint={constraint}
                        onAction={props.onConstraintAction}
                        readonly={props.readonly}
                    />)}
                />
                <ConstraintSection
                    title={"Nastavení trojitých směn"}
                    constraintsOfType={props.sortedConstraints["TRIPLE_SHIFTS_CONSTRAINT"]}
                    onConstraintAction={props.onConstraintAction}
                    defaultNewConstraint={() => generateNewConstraint(props.employee.id, ConstraintType.TRIPLE_SHIFTS_CONSTRAINT)}
                    renderer={(constraint) => (<TripleShiftConstraintForm
                        key={constraint.id}
                        constraint={constraint}
                        onAction={props.onConstraintAction}
                        readonly={props.readonly}
                    />)}
                />
                <ConstraintSection
                    title={"Nastaveni zakazanych kombinaci smen"}
                    constraintsOfType={props.sortedConstraints["SHIFT_FOLLOW_UP_RESTRICTION"]}
                    onConstraintAction={props.onConstraintAction}
                    defaultNewConstraint={() => ({
                        ...defaultConstraints["SHIFT_FOLLOW_UP_RESTRICTION"].constraint,
                        id: generateUUID(),
                        owner: props.employee.id
                    })}
                    renderer={constraint => (<ShiftFollowupRestrictionForm
                        key={constraint.id}
                        constraint={constraint}
                        onAction={props.onConstraintAction}
                        readonly={props.readonly}
                    />)}
                />
                <ConstraintSection
                    title={"Nastaveni poctu po sobe jdoucich smen"}
                    constraintsOfType={props.sortedConstraints["CONSECUTIVE_WORKING_DAYS"]}
                    onConstraintAction={props.onConstraintAction}
                    defaultNewConstraint={() => ({
                        ...defaultConstraints["CONSECUTIVE_WORKING_DAYS"].constraint,
                        id: generateUUID(),
                        owner: props.employee.id
                    })}
                    renderer={constraint => (<ConsecutiveWorkingDaysForm
                        key={constraint.id}
                        constraint={constraint}
                        onAction={props.onConstraintAction}
                        readonly={props.readonly}
                    />)}
                />
                <ConstraintSection
                    title={"Nastaveni víkendů"}
                    constraintsOfType={props.sortedConstraints["WEEKEND_CONSTRAINT"]}
                    onConstraintAction={props.onConstraintAction}
                    defaultNewConstraint={() => generateNewConstraint(props.employee.id, ConstraintType.WEEKEND_CONSTRAINT)}
                    renderer={constraint => (<WeekendConstraintForm
                        key={constraint.id}
                        constraint={constraint}
                        onAction={props.onConstraintAction}
                        readonly={props.readonly}
                    />)}
                />
                <ConstraintSection
                    title={"Nastaveni rozložení směn"}
                    constraintsOfType={props.sortedConstraints["EVEN_SHIFT_DISTRIBUTION"]}
                    onConstraintAction={props.onConstraintAction}
                    defaultNewConstraint={() => generateNewConstraint(props.employee.id, ConstraintType.EVEN_SHIFT_DISTRIBUTION)}
                    renderer={constraint => (<EvenDistributionConstraintForm
                        key={constraint.id}
                        constraint={constraint}
                        onAction={props.onConstraintAction}
                        readonly={props.readonly}
                    />)}
                />
                <HorizontalLayout style={{width: "100%", justifyContent: "end"}}>
                    <Button onClick={() => props.onOpenChanged(false)}>Zavřít</Button>
                </HorizontalLayout>
            </VerticalLayout>
        </Dialog>
    );
}

function generateNewConstraint(employeeId: string, type: ConstraintType): { owner: string } & ConstraintDTO {
    return {
        ...defaultConstraints[type].constraint,
        owner: employeeId,
        id: generateUUID()
    }
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
