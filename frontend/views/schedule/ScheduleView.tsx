import {DatePicker} from "@hilla/react-components/DatePicker";
import {Fragment, useContext, useEffect, useState} from "react";
import {EmployeeEndpoint, SolverConfigurationEndpoint, SolverEndpoint} from "Frontend/generated/endpoints";
import {Button} from "@hilla/react-components/Button";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {TextField} from "@hilla/react-components/TextField";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {GridDisplayMode, ScheduleGridContainer} from "./components/schedulegrid/ScheduleGridContainer";
import {
    EmployeeRequestConfigDialog
} from "Frontend/views/schedule/components/employeesettings/EmployeeRequestConfigDialog";
import {areShiftRequestsSame, CrudAction, CRUDActions, generateUUID} from "Frontend/util/utils";
import {Notification} from "@hilla/react-components/Notification";
import {Card} from "Frontend/components/Card";
import {Icon} from "@hilla/react-components/Icon";
import {Subscription} from "@hilla/frontend";
import {ScheduleMode, ScheduleModeCtx} from "Frontend/views/schedule/ScheduleModeCtxProvider";
import ConsecutiveWorkingDaysRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/ConsecutiveWorkingDaysRequestDTO";
import EmployeesPerShiftRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeesPerShiftRequestDTO";
import ShiftFollowupRestrictionRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftFollowupRestrictionRequestDTO";
import ShiftPatternRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftPatternRequestDTO";
import {ScheduleSettingsDialog} from "Frontend/views/schedule/components/schedulesettings/ScheduleSettingsDialog";
import {exportToExcel} from "Frontend/util/excel";
import {HeaderStrip} from "Frontend/views/schedule/HeaderStrip";
import ShiftsPerScheduleRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftsPerScheduleRequestDTO";
import ConstraintRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintRequestDTO";
import {ValidationContext} from "Frontend/views/schedule/components/validation/ScheduleValidationCtxProvider";
import TripleShiftConstraintRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/TripleShiftConstraintRequestDTO";
import SolverConfigurationDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverConfigurationDTO";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverSolutionDTO";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintType";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";
import SolutionStatus from "Frontend/generated/com/cocroachden/planner/solver/api/SolutionStatus";
import EmployeeShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeeShiftRequestDTO";
import EmployeeId from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeId";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeDTO";
import AssignedEmployeeDTO from "Frontend/generated/com/cocroachden/planner/solver/api/AssignedEmployeeDTO";
import TeamAssignmentRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/TeamAssignmentRequestDTO";
import WeekendRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/WeekendRequestDTO";
import EvenShiftDistributionRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/EvenShiftDistributionRequestDTO";

type EmployeeConfigDialogParams = {
    isOpen: boolean,
    selectedEmployee?: EmployeeId
}

export type ResultCache = {
    results: SolverSolutionDTO[]
    selectedIndex: number
}

function handleUnload(e: Event) {
    e.preventDefault()
}

const RESULT_CACHE_SIZE = 5

export default function ScheduleView() {

    const modeCtx = useContext(ScheduleModeCtx);
    const validationCtx = useContext(ValidationContext)
    const [employees, setEmployees] = useState<EmployeeDTO[]>([])
    const [employeeConfigDialog, setEmployeeConfigDialog] = useState<EmployeeConfigDialogParams>({isOpen: false})
    const [isScheduleConfigDialogOpen, setIsScheduleConfigDialogOpen] = useState(false);
    const [resultCache, setResultCache] = useState<ResultCache>({results: [], selectedIndex: 0});
    const [resultSubscription, setResultSubscription] = useState<Subscription<SolverSolutionDTO> | undefined>();
    const [gridDisplayMode, setGridDisplayMode] = useState<GridDisplayMode>(GridDisplayMode.PLANNING);

    const [request, setRequest] = useState<SolverConfigurationDTO | undefined>();
    const [shiftRequests, setShiftRequests] = useState<EmployeeShiftRequestDTO[]>([])
    const [shiftPerScheduleRequests, setShiftPerScheduleRequests] = useState<ShiftsPerScheduleRequestDTO[]>([])
    const [consecutiveWorkingDaysRequests, setConsecutiveWorkingDaysRequests] = useState<ConsecutiveWorkingDaysRequestDTO[]>([]);
    const [employeesPerShiftRequests, setEmployeesPerShiftRequests] = useState<EmployeesPerShiftRequestDTO[]>([]);
    const [shiftFollowupRestrictionRequests, setShiftFollowupRestrictionRequests] = useState<ShiftFollowupRestrictionRequestDTO[]>([]);
    const [shiftPatternRequests, setShiftPatternRequests] = useState<ShiftPatternRequestDTO[]>([]);
    const [tripleShiftConstraintRequests, setTripleShiftConstraintRequests] = useState<TripleShiftConstraintRequestDTO[]>([]);
    const [teamAssignmentRequests, setTeamAssignmentRequests] = useState<TeamAssignmentRequestDTO[]>([]);
    const [weekendRequests, setWeekendRequests] = useState<WeekendRequestDTO[]>([]);
    const [evenDistributionRequests, setEvenDistributionRequests] = useState<EvenShiftDistributionRequestDTO[]>([]);

    useEffect(() => {
        EmployeeEndpoint.getAllEmployees().then(setEmployees)
        window.addEventListener("beforeunload", handleUnload)
        return () => {
            window.removeEventListener("beforeunload", handleUnload)
        }
    }, []);

    useEffect(() => {
        //validate requests when shift request is made
        validateRequest()
    }, [shiftRequests]);

    useEffect(() => {
        //validate requests when config dialog closes
        if (!employeeConfigDialog.isOpen && !isScheduleConfigDialogOpen) {
            validateRequest();
        }
    }, [employeeConfigDialog, isScheduleConfigDialogOpen]);

    function handleShiftPatternAction(action: CrudAction<ShiftPatternRequestDTO>) {
        setShiftPatternRequests(prevState => updateList(action, prevState))
    }

    function handleCancel() {
        handleFetchConfig(request?.id!)
    }

    function combineConstraints() {
        return [
            ...shiftRequests,
            ...shiftPatternRequests,
            ...employeesPerShiftRequests,
            ...shiftFollowupRestrictionRequests,
            ...shiftPerScheduleRequests,
            ...consecutiveWorkingDaysRequests,
            ...tripleShiftConstraintRequests,
            ...teamAssignmentRequests,
            ...weekendRequests,
            ...evenDistributionRequests
        ] as ConstraintRequestDTO[]
    }

    async function handleSave() {
        await SolverConfigurationEndpoint.save({
            ...request!,
            id: generateUUID(),
            constraints: combineConstraints()
        }).then(response => {
            handleFetchConfig(response)
            Notification.show("Konfigurace úspěšně uložena!", {
                position: "top-center",
                duration: 5000,
                theme: "success"
            })
        })
    }

    async function handleUpdate() {
        await SolverConfigurationEndpoint.save({
            ...request!,
            constraints: combineConstraints()
        }).then(response => {
            handleFetchConfig(response)
            Notification.show("Konfigurace upravena!", {
                position: "top-center",
                duration: 5000,
                theme: "success"
            })
        })
    }

    function handleFetchConfig(configId: string) {
        SolverConfigurationEndpoint.getConfiguration(configId).then(configResponse => {
            validationCtx.clear()
            setResultCache({selectedIndex: 0, results: []})
            setShiftRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.EMPLOYEE_SHIFT_REQUEST) as EmployeeShiftRequestDTO[]
            )
            setShiftPerScheduleRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.SHIFT_PER_SCHEDULE) as ShiftsPerScheduleRequestDTO[]
            )
            setConsecutiveWorkingDaysRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.CONSECUTIVE_WORKING_DAYS) as ConsecutiveWorkingDaysRequestDTO[]
            )
            setEmployeesPerShiftRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.EMPLOYEES_PER_SHIFT) as EmployeesPerShiftRequestDTO[]
            )
            setShiftFollowupRestrictionRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION) as ShiftFollowupRestrictionRequestDTO[]
            )
            setShiftPatternRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.SHIFT_PATTERN_CONSTRAINT) as ShiftPatternRequestDTO[]
            )
            setTripleShiftConstraintRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.TRIPLE_SHIFTS_CONSTRAINT) as TripleShiftConstraintRequestDTO[]
            )
            setTeamAssignmentRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.TEAM_ASSIGNMENT) as TeamAssignmentRequestDTO[]
            )
            setWeekendRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.WEEKEND_REQUEST) as WeekendRequestDTO[]
            )
            setEvenDistributionRequests(
                configResponse.constraints.filter(c => c.type === ConstraintType.EVEN_SHIFT_DISTRIBUTION) as EvenShiftDistributionRequestDTO[]
            )
            configResponse["constraints"] = []
            setRequest(configResponse)
        })
        modeCtx.setMode(ScheduleMode.READONLY)
    }

    function handleAssignmentAction(action: CrudAction<AssignedEmployeeDTO>) {
        switch (action.type) {
            case CRUDActions.READ:
                setEmployeeConfigDialog({
                    selectedEmployee: {id: action.payload.employee.id},
                    isOpen: true
                })
                break
            case CRUDActions.UPDATE:
                setRequest(prevState => {
                    if (prevState == undefined) return undefined
                    return {
                        ...request!,
                        employees: updateAssignmentsWithIndexIntegrity(
                            action.payload,
                            request!.employees
                        )
                    }
                })
                break
            case CRUDActions.CREATE:
                setRequest(prevState => {
                    if (prevState == undefined) return undefined
                    return {
                        ...request!,
                        employees: [...request!.employees, action.payload]
                    }
                })
                break
            case CRUDActions.DELETE:
                setRequest(prevState => {
                    if (prevState == undefined) return undefined
                    return {
                        ...request!,
                        employees: request!.employees.filter(a => a.employee.id !== action.payload.employee.id)
                    }
                })
        }
    }

    function handleShiftRequestsChanged(changedRequests: Omit<EmployeeShiftRequestDTO, "id">[]) {
        setShiftRequests(prevState => {
            return [
                ...prevState.filter(r => !changedRequests.some(changed => areShiftRequestsSame(r, changed))),
                ...changedRequests.filter(r => r.requestedShift !== WorkShifts.ANY).map(r => ({
                    ...r,
                    id: generateUUID()
                }))
            ]
        })
    }

    function handleStopCalculation() {
        if (resultSubscription) {
            resultSubscription.cancel()
        }
        setResultSubscription(undefined)
        modeCtx.setMode(ScheduleMode.READONLY)
    }

    function validateRequest() {
        validationCtx.validate({...request!, constraints: combineConstraints()})
    }

    function handleStartCalculation() {
        handleStopCalculation()
        setGridDisplayMode(GridDisplayMode.RESULT)
        setResultSubscription(SolverEndpoint.solve(request?.id)
            .onNext(value => {
                if (value.solutionStatus !== SolutionStatus.OK) {
                    Notification.show("Neřešitelné zadání!", {
                        position: "top-center",
                        duration: 5000,
                        theme: "error"
                    })
                    return
                }
                setResultCache(prevState => {
                    const updatedResults = [...prevState.results, value].slice(-RESULT_CACHE_SIZE)
                    return {
                        results: updatedResults,
                        selectedIndex: updatedResults.length - 1
                    }
                });
            }).onComplete(() => {
                Notification.show("Výpočet ukončen!", {
                    position: "top-center",
                    duration: 5000,
                    theme: "success"
                })
                handleStopCalculation()
            }).onError(() => {
                Notification.show("Neřešitelné zadání!", {
                    position: "top-center",
                    duration: 5000,
                    theme: "error"
                })
                handleStopCalculation()
            })
        )
    }

    function handleResultSelectionChanged(offset: 1 | -1) {
        setResultCache(prevState => ({
            ...prevState,
            selectedIndex: Math.min(RESULT_CACHE_SIZE - 1, Math.max(0, prevState.selectedIndex + offset))
        }))
    }

    function handleTripleShiftConstraintAction(action: CrudAction<TripleShiftConstraintRequestDTO>) {
        setTripleShiftConstraintRequests(prevState => updateList(action, prevState))
    }

    function handleEmployeePerShiftAction(action: CrudAction<EmployeesPerShiftRequestDTO>) {
        setEmployeesPerShiftRequests(prevState => updateList(action, prevState))
    }

    function handleShiftFollowupRestrictionAction(action: CrudAction<ShiftFollowupRestrictionRequestDTO>) {
        setShiftFollowupRestrictionRequests(prevState => updateList(action, prevState))
    }

    function handleConsecutiveWorkingDaysAction(action: CrudAction<ConsecutiveWorkingDaysRequestDTO>) {
        setConsecutiveWorkingDaysRequests(prevState => updateList(action, prevState))
    }

    function handleShiftPerScheduleAction(action: CrudAction<ShiftsPerScheduleRequestDTO>) {
        setShiftPerScheduleRequests(prevState => updateList(action, prevState))
    }

    function handleWeekendRequestAction(action: CrudAction<WeekendRequestDTO>) {
        setWeekendRequests(prevState => updateList(action, prevState))
    }

    function handleEvenDistributionRequestAction(action: CrudAction<EvenShiftDistributionRequestDTO>) {
        setEvenDistributionRequests(prevState => updateList(action, prevState))
    }

    function handleTeamAssignmentAction(action: CrudAction<TeamAssignmentRequestDTO>) {
        function updateTeamAssignments(action: CrudAction<TeamAssignmentRequestDTO>, requests: TeamAssignmentRequestDTO[]) {
            switch (action.type) {
                case CRUDActions.UPDATE:
                    const isNewLeader = action.payload.isLeader
                    const other = requests.filter(r => r.teamId !== action.payload.teamId)
                    const sameTeamAssignments = requests
                        .filter(r => r.teamId === action.payload.teamId)
                        .map(r => {
                            if (r.id === action.payload.id) {
                                return action.payload
                            }
                            return isNewLeader ? { ...r, isLeader: false } : r
                        })
                    return [ ...other, ...sameTeamAssignments ]
                case CRUDActions.DELETE:
                    return requests.filter(r => r.id !== action.payload.id)
                case CRUDActions.CREATE:
                    return [...requests, action.payload]
                default:
                    return requests
            }
        }

        setTeamAssignmentRequests(prevState => updateTeamAssignments(action, prevState))
    }

    function renderGridHeader() {
        return (
            <Card style={{width: "100%"}}>
                <HorizontalLayout theme={"spacing"} style={{alignItems: "end"}}>
                    <TextField
                        label={"Název"}
                        value={request?.name}
                        onChange={e => setRequest({...request!, name: e.target.value})}
                        readonly={modeCtx.mode !== ScheduleMode.EDIT}
                        disabled={!request}
                        style={{width: 385}}
                    />
                    <DatePicker
                        label={"Od"}
                        value={request && request?.startDate}
                        onChange={e => setRequest({
                            ...request!,
                            startDate: e.target.value
                        })}
                        readonly={modeCtx.mode !== ScheduleMode.EDIT}
                        disabled={!request}
                    />
                    <DatePicker
                        label={"Do"}
                        value={request && request?.endDate}
                        onChange={e => setRequest({
                            ...request!,
                            endDate: e.target.value
                        })}
                        readonly={modeCtx.mode !== ScheduleMode.EDIT}
                        disabled={!request}
                    />
                    <Button
                        theme={"secondary"}
                        onClick={() => setIsScheduleConfigDialogOpen(true)}
                    >
                        <Icon style={{marginRight: 5}} icon={"vaadin:cog"}/>
                        Nastavení
                    </Button>
                </HorizontalLayout>
            </Card>
        )
    }

    return (
        <VerticalLayout theme={"spacing padding"}>
            <HeaderStrip
                onStopCalculation={handleStopCalculation}
                onStartCalculation={handleStartCalculation}
                resultSubscription={resultSubscription}
                request={request}
                onConfigSelected={handleFetchConfig}
                onValidateRequest={validateRequest}
                resultCache={resultCache}
                onSave={handleSave}
                onUpdate={handleUpdate}
                onCancel={handleCancel}
                onClearCache={() => setResultCache({results: [], selectedIndex: 0})}
                onExportToExcel={() => exportToExcel(request!.name, request!.employees, resultCache.results[resultCache.selectedIndex])}
                cacheSize={RESULT_CACHE_SIZE}
                onResultSelectionChanged={handleResultSelectionChanged}
                gridDisplayMode={gridDisplayMode}
                onGridDisplayModeChange={setGridDisplayMode}
            />
            {request ? renderGridHeader() : <h2 style={{marginTop: 30, padding: 10}}>Vyberte rozvrh</h2>}
            {request &&
                <Fragment>
                    <ScheduleSettingsDialog
                        isOpen={isScheduleConfigDialogOpen}
                        onOpenChanged={setIsScheduleConfigDialogOpen}
                        request={request}
                        employees={employees}
                        onAssignmentAction={handleAssignmentAction}
                        employeesPerShift={employeesPerShiftRequests}
                        onEmployeePerShiftAction={handleEmployeePerShiftAction}
                        shiftFollowupRestriction={shiftFollowupRestrictionRequests}
                        onShiftFollowupRestrictionAction={handleShiftFollowupRestrictionAction}
                        consecutiveWorkingDays={consecutiveWorkingDaysRequests}
                        onConsecutiveWorkingDaysAction={handleConsecutiveWorkingDaysAction}
                    />
                    <EmployeeRequestConfigDialog
                        key={employeeConfigDialog.selectedEmployee?.id}
                        assignment={request.employees.find(w => w.employee.id === employeeConfigDialog.selectedEmployee?.id)!}
                        isOpen={employeeConfigDialog.isOpen}
                        onShiftPerScheduleAction={handleShiftPerScheduleAction}
                        shiftsPerScheduleRequests={shiftPerScheduleRequests.filter(r => r.owner.id === employeeConfigDialog.selectedEmployee?.id)}
                        onOpenChanged={(newValue) => setEmployeeConfigDialog(prevState => ({
                            ...prevState,
                            isOpen: newValue
                        }))}
                        shiftPatternRequests={shiftPatternRequests.filter(r => r.owner!.id === employeeConfigDialog.selectedEmployee?.id)}
                        onShiftPatternRequestsAction={handleShiftPatternAction}
                        tripleShiftConstraintRequest={tripleShiftConstraintRequests.filter(r => r.owner.id === employeeConfigDialog.selectedEmployee?.id)}
                        onTripleShiftConstraintAction={handleTripleShiftConstraintAction}
                        teamAssignmentRequests={teamAssignmentRequests.filter(r => r.owner.id === employeeConfigDialog.selectedEmployee?.id)}
                        onTeamAssignmentRequestAction={handleTeamAssignmentAction}
                        onAssignmentAction={handleAssignmentAction}
                        weekendRequests={weekendRequests.filter(r => r.owner.id === employeeConfigDialog.selectedEmployee?.id)}
                        onWeekendRequestRequestAction={handleWeekendRequestAction}
                        evenDistributionRequests={evenDistributionRequests}
                        onEvenDistributionRequestsAction={handleEvenDistributionRequestAction}
                        readonly={modeCtx.mode !== ScheduleMode.EDIT}
                    />
                    <Card
                        style={{
                            borderWidth: 1,
                            borderColor: "var(--lumo-shade-70pct)",
                            height: "570px",
                            width: "100%",
                            overflow: "scroll"
                        }}
                    >
                        <ScheduleGridContainer
                            request={request}
                            shiftRequests={shiftRequests}
                            shiftPatterns={shiftPatternRequests}
                            shiftPerScheduleRequests={shiftPerScheduleRequests}
                            teamAssignments={teamAssignmentRequests}
                            onAssignmentAction={handleAssignmentAction}
                            onShiftRequestsChanged={handleShiftRequestsChanged}
                            result={resultCache.results.length > 0 ? resultCache.results[resultCache.selectedIndex] : undefined}
                            displayMode={gridDisplayMode}
                        />
                    </Card>
                </Fragment>
            }
        </VerticalLayout>
    )
}

function updateList<T extends { id: string }>(action: CrudAction<T>, requests: T[]): T[] {
    switch (action.type) {
        case CRUDActions.UPDATE:
            return requests.map(r => {
                if (action.payload.id !== r.id) return r
                return action.payload
            })
        case CRUDActions.DELETE:
            return requests.filter(r => r.id !== action.payload.id)
        case CRUDActions.CREATE:
            return [...requests, action.payload]
        default:
            return requests
    }
}

function updateAssignmentsWithIndexIntegrity(updated: AssignedEmployeeDTO, previousState: AssignedEmployeeDTO[]) {
    const original = previousState.find(a => a.employee.id === updated.employee.id)
    if (!original) {
        return [...previousState, updated]
            .sort((a, b) => a.index - b.index)
            .map((value, index) => ({...value, index}))
    }
    const neighbor = previousState.find(a => {
        return a.employee.id != updated.employee.id && a.index === updated.index
    })
    if (!neighbor) {
        return previousState
            .map(a => {
                if (a.employee.id === updated.employee.id) {
                    return updated
                }
                return a
            }).sort((a, b) => a.index - b.index)
            .map((value, index) => ({...value, index}))
    }
    const offset = updated.index - original.index
    return previousState.map(a => {
        if (a.employee.id === updated.employee.id) {
            return updated
        }
        if (a.employee.id === neighbor.employee.id) {
            return {...neighbor, index: neighbor.index - offset}
        }
        return a
    }).sort((a, b) => a.index - b.index)
        .map((value, index) => ({...value, index}));
}