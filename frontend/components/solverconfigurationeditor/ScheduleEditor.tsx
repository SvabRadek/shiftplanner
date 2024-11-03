import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import {Fragment, useEffect, useMemo, useState} from "react";
import {
    GridDisplayMode,
    ScheduleGridContainer
} from "Frontend/components/solverconfigurationeditor/grid/schedulegrid/ScheduleGridContainer";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import {
    defaultEmptySortedConstraints,
    sortConstraints,
    SortedConstraints,
    sortedConstraintsForEmployee
} from "Frontend/views/schedule/ConstraintUtils";
import {
    EmployeeConstraintsDialog
} from "Frontend/components/solverconfigurationeditor/dialogs/EmployeeConstraintsDialog";
import {CrudAction, CRUDActions, generateUUID} from "Frontend/util/utils";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import ConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintDTO";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {Icon} from "@hilla/react-components/Icon";
import {TextField} from "@hilla/react-components/TextField";
import {DatePicker} from "@hilla/react-components/DatePicker";
import {PrimaryButton} from "Frontend/components/PrimaryButton";
import {SecondaryButton} from "Frontend/components/SecondaryButton";
import {ScheduleSettingsDialog} from "Frontend/components/solverconfigurationeditor/dialogs/ScheduleSettingsDialog";
import RequestedShiftConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/RequestedShiftConstraintDTO";
import {PartialBy} from "Frontend/util/types";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintType";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/SolverSolutionDTO";

function reiterateIndexes(assignments: EmployeeAssignmentDTO[]): EmployeeAssignmentDTO[] {
    for (let i = 0; i < assignments.length; i++) {
        assignments[i].index = i
    }
    return assignments
}

function updateAssignments(updated: EmployeeAssignmentDTO, assignments: EmployeeAssignmentDTO[]): EmployeeAssignmentDTO[] {
    const updatedAssignments = assignments.filter(a => a.employeeId !== updated.employeeId)
    if (updated.index === 0) {
        return reiterateIndexes([updated, ...updatedAssignments])
    } else if (updated.index >= updatedAssignments.length) {
        return reiterateIndexes([...updatedAssignments, updated]);
    } else {
        const before = updatedAssignments.slice(0, updated.index)
        const after = updatedAssignments.slice(updated.index);
        return reiterateIndexes([...before, updated, ...after])
    }
}

type EmployeeDialogProps = {
    isOpen: boolean
    employeeId: string | undefined
}

type Props = {
    configuration: SolverConfigurationDTO
    onConfigurationChanged: (changed: SolverConfigurationDTO) => void
    onClose: () => void
    employees: EmployeeDTO[]
    readonly: boolean
    solution?: SolverSolutionDTO
}

export function ScheduleEditor(props: Props) {

    const [employeeDialog, setEmployeeDialog] = useState<EmployeeDialogProps>({isOpen: false, employeeId: undefined});
    const [configDialog, setConfigDialog] = useState(false);
    const [showingSolutions, setShowingSolutions] = useState(false);

    const isReadOnly = showingSolutions || props.readonly

    const sortedConstraints = useMemo<SortedConstraints>(() => {
        return sortConstraints(props.configuration.constraints)
    }, [props.configuration.constraints])

    const sortedConstraintsForSelectedEmployee = useMemo<SortedConstraints>(() => {
        if (employeeDialog.employeeId) {
            return sortedConstraintsForEmployee(employeeDialog.employeeId, sortedConstraints)
        }
        return defaultEmptySortedConstraints
    }, [employeeDialog.employeeId, sortedConstraints]);

    function handleConfigurationUpdate(updated: Partial<SolverConfigurationDTO>) {
        props.onConfigurationChanged({...props.configuration, ...updated})
    }

    function handleAssignmentAction(action: CrudAction<EmployeeAssignmentDTO>) {
        switch (action.type) {
            case CRUDActions.READ:
                setEmployeeDialog({employeeId: action.payload.employeeId, isOpen: true})
                break
            case CRUDActions.UPDATE:
                props.onConfigurationChanged({
                    ...props.configuration,
                    employees: updateAssignments(action.payload, props.configuration.employees)
                })
                break
            case CRUDActions.CREATE:
                props.onConfigurationChanged({
                    ...props.configuration,
                    employees: reiterateIndexes([...props.configuration.employees, action.payload]),
                })
                break
            case CRUDActions.DELETE:
                props.onConfigurationChanged({
                    ...props.configuration,
                    employees: reiterateIndexes(props.configuration.employees.filter(a => a.employeeId !== action.payload.employeeId)),
                })
        }
    }

    function handleBulkRequestedShiftConstraint(constraints: PartialBy<RequestedShiftConstraintDTO, "id">[]) {
        const irrelevantConstraints: ConstraintDTO[] = []
        const existingShiftRequestConstraints: RequestedShiftConstraintDTO[] = []
        props.configuration.constraints.forEach(c => {
            if (c.type === ConstraintType.REQUESTED_SHIFT_CONSTRAINT) {
                existingShiftRequestConstraints.push(c as RequestedShiftConstraintDTO)
            } else {
                irrelevantConstraints.push(c)
            }
        })
        const updatedList: RequestedShiftConstraintDTO[] = []
        const createdList: RequestedShiftConstraintDTO[] = []
        constraints.forEach(c => {
            if (!c.id) {
                createdList.push({...c, id: generateUUID()})
            } else {
                updatedList.push({...c, id: c.id})
            }
        })
        const updatedRelevantConstraints = [
            ...existingShiftRequestConstraints.map(existing => {
                const updated = updatedList.find(c => existing.id === c.id)
                return updated ? updated : existing
            }),
            ...createdList
        ].filter(c => c.requestedShift !== WorkShifts.ANY)
        props.onConfigurationChanged({
            ...props.configuration,
            constraints: [...irrelevantConstraints, ...updatedRelevantConstraints]
        })
    }

    function handleConstraintAction(action: CrudAction<ConstraintDTO>) {
        switch (action.type) {
            case CRUDActions.CREATE:
                props.onConfigurationChanged({
                    ...props.configuration,
                    constraints: [...props.configuration.constraints, action.payload]
                })
                break;
            case CRUDActions.READ:
                break;
            case CRUDActions.UPDATE:
                props.onConfigurationChanged({
                    ...props.configuration,
                    constraints: props.configuration.constraints.map(c => c.id === action.payload.id ? action.payload : c)
                })
                break;
            case CRUDActions.DELETE:
                props.onConfigurationChanged({
                    ...props.configuration,
                    constraints: props.configuration.constraints.filter(c => c.id !== action.payload.id)
                })
                break;
        }
    }

    return (
        <VerticalLayout theme={"spacing"} style={{width: "100%"}}>
            <HorizontalLayout theme={"spacing"} style={{width: "100%", alignItems: "end"}}>
                <TextField
                    label={"Název"}
                    value={props.configuration.name}
                    onChange={e => handleConfigurationUpdate({name: e.target.value})}
                    style={{width: 385}}
                />
                <DatePicker
                    label={"Od"}
                    value={props.configuration.startDate}
                    onChange={e => handleConfigurationUpdate({startDate: e.target.value})}
                />
                <DatePicker
                    label={"Do"}
                    value={props.configuration.endDate}
                    onChange={e => handleConfigurationUpdate({endDate: e.target.value})}
                />
                <HorizontalLayout style={{width: "100%", justifyContent: "space-between"}} theme={"spacing"}>
                    <HorizontalLayout theme={"spacing"}>
                        <PrimaryButton onClick={() => setConfigDialog(true)}>
                            <Icon icon={"vaadin:cogs"} slot={"prefix"}/>
                            Možnosti
                        </PrimaryButton>
                        {props.solution &&
                            <PrimaryButton onClick={() => {
                                setShowingSolutions(prevState => !prevState)
                            }}>
                                <Icon icon={"vaadin:eye"} slot={"prefix"}/>
                                {showingSolutions ? "Zobrazit plan" : "Zobrazit vysledky"}
                            </PrimaryButton>
                        }
                    </HorizontalLayout>
                    <SecondaryButton onClick={props.onClose}>
                        <Icon icon={"vaadin:close"} slot={"prefix"}/>
                        Zavřít
                    </SecondaryButton>
                </HorizontalLayout>
            </HorizontalLayout>
            {props.configuration &&
                <Fragment>
                    <ScheduleSettingsDialog
                        isOpen={configDialog}
                        onOpenChanged={setConfigDialog}
                        solverConfiguration={props.configuration}
                        employees={props.employees}
                        onAssignmentAction={handleAssignmentAction}
                        sortedConstraints={sortedConstraints}
                        onConstraintAction={handleConstraintAction}
                        readonly={props.readonly}
                    />
                    <EmployeeConstraintsDialog
                        isOpen={employeeDialog.isOpen}
                        onOpenChanged={(newValue) => setEmployeeDialog(prevState => ({...prevState, isOpen: newValue}))}
                        employee={props.employees.find(e => e.id === employeeDialog.employeeId)!}
                        assignment={props.configuration.employees.find(w => w.employeeId === employeeDialog.employeeId)!}
                        onAssignmentAction={handleAssignmentAction}
                        sortedConstraints={sortedConstraintsForSelectedEmployee}
                        onConstraintAction={handleConstraintAction}
                        readonly={isReadOnly}
                    />
                    <ScheduleGridContainer
                        solverConfiguration={props.configuration}
                        employees={props.employees}
                        sortedConstraints={sortedConstraints}
                        onConstraintAction={handleConstraintAction}
                        onAssignmentAction={handleAssignmentAction}
                        onShiftRequestsChanged={handleBulkRequestedShiftConstraint}
                        solution={props.solution}
                        readOnly={isReadOnly}
                        displayMode={showingSolutions ? GridDisplayMode.RESULT : GridDisplayMode.PLANNING}/>
                </Fragment>
            }
        </VerticalLayout>
    );
}
