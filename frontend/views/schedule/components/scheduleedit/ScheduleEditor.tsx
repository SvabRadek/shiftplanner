import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import {Fragment, useContext, useMemo, useState} from "react";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintType";
import {
    GridDisplayMode,
    ScheduleGridContainer
} from "Frontend/views/schedule/components/schedulegrid/ScheduleGridContainer";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import {sortConstraints, SortedConstraints} from "Frontend/views/schedule/ConstraintUtils";
import {ScheduleMode, ScheduleModeCtx} from "Frontend/views/schedule/ScheduleModeCtxProvider";
import {EmployeeConstraintsDialog} from "Frontend/views/schedule/components/employeesettings/EmployeeConstraintsDialog";
import {CrudAction, CRUDActions} from "Frontend/util/utils";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";


function reiterateIndexes(assignments: EmployeeAssignmentDTO[]) {
    for (let i = 0; i < assignments.length; i++) {
        assignments[i].index = i
    }
    return assignments
}

function updateAssignments(updated: EmployeeAssignmentDTO, assignments: EmployeeAssignmentDTO[]) {
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
    onConfigurationAction: (action: SolverConfigurationDTO) => void
    employees: EmployeeDTO[]
}

export function ScheduleEditor(props: Props) {

    const [employeeDialog, setEmployeeDialog] = useState<EmployeeDialogProps>({ isOpen: false, employeeId: undefined });
    const modeCtx = useContext(ScheduleModeCtx);
    const sortedConstraints = useMemo<SortedConstraints>(() => {
        return sortConstraints(props.configuration.constraints)
    }, [props.configuration.constraints])

    function handleAssignmentAction(action: CrudAction<EmployeeAssignmentDTO>) {
        switch (action.type) {
            case CRUDActions.READ:
                setEmployeeDialog({employeeId: action.payload.employeeId, isOpen: true})
                break
            case CRUDActions.UPDATE:
                props.onConfigurationAction({
                    ...props.configuration,
                    employees: updateAssignments(action.payload, props.configuration.employees)
                })
                break
            case CRUDActions.CREATE:
                props.onConfigurationAction({
                    ...props.configuration,
                    employees: reiterateIndexes([...props.configuration.employees, action.payload]),
                })
                break
            case CRUDActions.DELETE:
                props.onConfigurationAction({
                        ...props.configuration,
                        employees: reiterateIndexes(props.configuration.employees.filter(a => a.employeeId !== action.payload.employeeId)),
                })
        }
    }

    return (
        <VerticalLayout theme={"spacing padding"} style={{width: "100%"}}>
            {props.configuration &&
                <Fragment>
                    <EmployeeConstraintsDialog
                        assignment={props.configuration.employees.find(w => w.employeeId === employeeDialog.employeeId)!}
                        employee={props.employees.find(e => e.id === employeeDialog.employeeId)!}
                        isOpen={employeeDialog.isOpen}
                        onShiftPerScheduleAction={handleShiftPerScheduleAction}
                        shiftsPerScheduleRequests={sortedConstraints["SHIFTS_PER_SCHEDULE"].filter(r => r.owner === employeeDialog.employeeId)}
                        onOpenChanged={(newValue) => setEmployeeDialog(prevState => ({ ...prevState, isOpen: newValue }))}
                        shiftPatternRequests={sortedConstraints["SHIFT_PATTERN_CONSTRAINT"].filter(r => r.owner === employeeDialog.employeeId)}
                        onShiftPatternRequestsAction={handleShiftPatternAction}
                        tripleShiftConstraintRequest={sortedConstraints["TRIPLE_SHIFTS_CONSTRAINT"].filter(r => r.owner === employeeDialog.employeeId)}
                        onTripleShiftConstraintAction={handleTripleShiftConstraintAction}
                        teamAssignmentRequests={sortedConstraints["TEAM_ASSIGNMENT"].filter(r => r.owner === employeeDialog.employeeId)}
                        onTeamAssignmentRequestAction={handleTeamAssignmentAction}
                        onAssignmentAction={handleAssignmentAction}
                        weekendRequests={sortedConstraints["WEEKEND_CONSTRAINT"].filter(r => r.owner === employeeDialog.employeeId)}
                        onWeekendRequestRequestAction={handleWeekendRequestAction}
                        evenDistributionRequests={sortedConstraints["EVEN_SHIFT_DISTRIBUTION"]}
                        onEvenDistributionRequestsAction={handleEvenDistributionRequestAction}
                        readonly={modeCtx.mode !== ScheduleMode.EDIT}
                    />
                    <ScheduleGridContainer
                        solverConfiguration={props.configuration}
                        employees={props.employees}
                        shiftPatterns={sortedConstraints[ConstraintType.SHIFT_PATTERN_CONSTRAINT]}
                        shiftRequests={sortedConstraints[ConstraintType.REQUESTED_SHIFT_CONSTRAINT]}
                        teamAssignments={sortedConstraints[ConstraintType.TEAM_ASSIGNMENT]}
                        shiftPerScheduleRequests={sortedConstraints[ConstraintType.SHIFTS_PER_SCHEDULE]}
                        onAssignmentAction={handleAssignmentAction}
                        displayMode={GridDisplayMode.PLANNING}/>
                </Fragment>

            }
        </VerticalLayout>
    );
}
