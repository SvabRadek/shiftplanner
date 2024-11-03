import {Card} from "Frontend/components/Card";
import {EmployeesPerShiftForm} from "Frontend/components/constraint/forms/EmployeesPerShiftForm";
import {CrudAction, generateUUID} from "Frontend/util/utils";
import {defaultConstraints} from "Frontend/views/schedule/DefaultEmptyConstraints";
import "@vaadin/icons";
import {SortedConstraints} from "Frontend/views/schedule/ConstraintUtils";
import ConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintDTO";
import {ConstraintSection} from "Frontend/components/constraint/ConstraintSection";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintType";

type Props = {
    sortedConstraints: SortedConstraints
    onConstraintAction: (action: CrudAction<ConstraintDTO>) => void
    readonly: boolean
}

export function GlobalTab(props: Props) {

    return (
        <Card theme={"spacing padding"}>
            <ConstraintSection
                constraintsOfType={props.sortedConstraints["EMPLOYEES_PER_SHIFT"]}
                onConstraintAction={props.onConstraintAction}
                title={"Pocet zamestnancu pridelenych na smenu"}
                defaultNewConstraint={() => ({
                    ...defaultConstraints[ConstraintType.EMPLOYEES_PER_SHIFT].constraint,
                    id: generateUUID()
                })}
                renderer={constraint => (
                    <EmployeesPerShiftForm
                        key={constraint.id}
                        constraint={constraint}
                        excludedShifts={props.sortedConstraints["EMPLOYEES_PER_SHIFT"].map(r => r.targetShift)}
                        onAction={props.onConstraintAction}
                        readonly={props.readonly}
                    />)}
            />
        </Card>
    );
}
