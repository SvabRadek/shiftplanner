import {DatePicker} from "@hilla/react-components/DatePicker";
import {Fragment, useContext, useEffect, useState} from "react";
import {
    EmployeeEndpoint,
    SolverConfigurationEndpoint,
    SolverEndpoint,
    TicketEndpoint
} from "Frontend/generated/endpoints";
import {Button} from "@hilla/react-components/Button";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {TextField} from "@hilla/react-components/TextField";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {GridDisplayMode, ScheduleGridContainer} from "./components/schedulegrid/ScheduleGridContainer";
import {EmployeeConstraintsDialog} from "Frontend/views/schedule/components/employeesettings/EmployeeConstraintsDialog";
import {areShiftRequestsSame, CrudAction, CRUDActions, generateUUID} from "Frontend/util/utils";
import {Notification} from "@hilla/react-components/Notification";
import {Card} from "Frontend/components/Card";
import {Icon} from "@hilla/react-components/Icon";
import {Subscription} from "@hilla/frontend";
import {ScheduleMode, ScheduleModeCtx} from "Frontend/views/schedule/ScheduleModeCtxProvider";
import {ScheduleSettingsDialog} from "Frontend/views/schedule/components/schedulesettings/ScheduleSettingsDialog";
import {exportToExcel} from "Frontend/util/excel";
import {HeaderStrip} from "Frontend/views/schedule/HeaderStrip";
import {ValidationContext} from "Frontend/views/schedule/components/validation/ScheduleValidationCtxProvider";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";
import "@vaadin/icons";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import ShiftsPerScheduleConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleConstraintDTO";
import ConsecutiveWorkingDaysConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/ConsecutiveWorkingDaysConstraintDTO";
import EmployeesPerShiftConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftConstraintDTO";
import ShiftFollowupRestrictionConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionConstraintDTO";
import ShiftPatternConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ShiftPatternConstraintDTO";
import TripleShiftConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/TripleShiftConstraintDTO";
import TeamAssignmentConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/TeamAssignmentConstraintDTO";
import WeekendConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/WeekendConstraintDTO";
import EvenShiftDistributionConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/EvenShiftDistributionConstraintDTO";
import ConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintDTO";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintType";
import RequestedShiftConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/RequestedShiftConstraintDTO";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import SolutionStatus from "Frontend/generated/com/cocroachden/planner/solver/SolutionStatus";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/SolverSolutionDTO";
import {sortConstraints} from "Frontend/views/schedule/ConstraintUtils";

type EmployeeConfigDialogParams = {
    isOpen: boolean,
    selectedEmployeeId?: string
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

    const [solverConfiguration, setSolverConfiguration] = useState<SolverConfigurationDTO | undefined>();
    const [requestedShiftConstraints, setRequestedShiftConstraints] = useState<RequestedShiftConstraintDTO[]>([])
    const [shiftPerScheduleRequests, setShiftPerScheduleRequests] = useState<ShiftsPerScheduleConstraintDTO[]>([])
    const [consecutiveWorkingDaysRequests, setConsecutiveWorkingDaysRequests] = useState<ConsecutiveWorkingDaysConstraintDTO[]>([]);
    const [employeesPerShiftRequests, setEmployeesPerShiftRequests] = useState<EmployeesPerShiftConstraintDTO[]>([]);
    const [shiftFollowupRestrictionRequests, setShiftFollowupRestrictionRequests] = useState<ShiftFollowupRestrictionConstraintDTO[]>([]);
    const [shiftPatternRequests, setShiftPatternRequests] = useState<ShiftPatternConstraintDTO[]>([]);
    const [tripleShiftConstraintRequests, setTripleShiftConstraintRequests] = useState<TripleShiftConstraintDTO[]>([]);
    const [teamAssignmentRequests, setTeamAssignmentRequests] = useState<TeamAssignmentConstraintDTO[]>([]);
    const [weekendRequests, setWeekendRequests] = useState<WeekendConstraintDTO[]>([]);
    const [evenDistributionRequests, setEvenDistributionRequests] = useState<EvenShiftDistributionConstraintDTO[]>([]);

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
    }, [requestedShiftConstraints]);

    useEffect(() => {
        //validate requests when config dialog closes
        if (!employeeConfigDialog.isOpen && !isScheduleConfigDialogOpen) {
            validateRequest();
        }
    }, [employeeConfigDialog, isScheduleConfigDialogOpen]);

    function handleShiftPatternAction(action: CrudAction<ShiftPatternConstraintDTO>) {
        setShiftPatternRequests(prevState => updateList(action, prevState))
    }

    function handleCancel() {
        handleFetchConfig(solverConfiguration?.id!)
    }

    function combineConstraints() {
        return [
            ...requestedShiftConstraints,
            ...shiftPatternRequests,
            ...employeesPerShiftRequests,
            ...shiftFollowupRestrictionRequests,
            ...shiftPerScheduleRequests,
            ...consecutiveWorkingDaysRequests,
            ...tripleShiftConstraintRequests,
            ...teamAssignmentRequests,
            ...weekendRequests,
            ...evenDistributionRequests
        ] as ConstraintDTO[]
    }

    async function handleSave() {
        await SolverConfigurationEndpoint.save({
            ...solverConfiguration!,
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
        await SolverConfigurationEndpoint.update({
            ...solverConfiguration!,
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
            const sortedConstraints = sortConstraints(configResponse.constraints)
            setRequestedShiftConstraints(sortedConstraints[ConstraintType.REQUESTED_SHIFT_CONSTRAINT])
            setShiftPerScheduleRequests(sortedConstraints[ConstraintType.SHIFTS_PER_SCHEDULE])
            setConsecutiveWorkingDaysRequests(sortedConstraints[ConstraintType.CONSECUTIVE_WORKING_DAYS])
            setEmployeesPerShiftRequests(sortedConstraints[ConstraintType.EMPLOYEES_PER_SHIFT])
            setShiftFollowupRestrictionRequests(sortedConstraints[ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION])
            setShiftPatternRequests(sortedConstraints[ConstraintType.SHIFT_PATTERN_CONSTRAINT])
            setTripleShiftConstraintRequests(sortedConstraints[ConstraintType.TRIPLE_SHIFTS_CONSTRAINT])
            setTeamAssignmentRequests(sortedConstraints[ConstraintType.TEAM_ASSIGNMENT])
            setWeekendRequests(sortedConstraints[ConstraintType.WEEKEND_CONSTRAINT])
            setEvenDistributionRequests(sortedConstraints[ConstraintType.EVEN_SHIFT_DISTRIBUTION])
            configResponse["constraints"] = []
            setSolverConfiguration(configResponse)
        })
        modeCtx.setMode(ScheduleMode.READONLY)
    }

    function handleAssignmentAction(action: CrudAction<EmployeeAssignmentDTO>) {
        switch (action.type) {
            case CRUDActions.READ:
                setEmployeeConfigDialog({
                    selectedEmployeeId: action.payload.employeeId,
                    isOpen: true
                })
                break
            case CRUDActions.UPDATE:
                setSolverConfiguration(prevState => {
                    if (prevState == undefined) return undefined
                    return {
                        ...solverConfiguration!,
                        employees: updateAssignmentsWithIndexIntegrity(
                            action.payload,
                            solverConfiguration!.employees
                        )
                    }
                })
                break
            case CRUDActions.CREATE:
                setSolverConfiguration(prevState => {
                    if (prevState == undefined) return undefined
                    return {
                        ...solverConfiguration!,
                        employees: [...solverConfiguration!.employees, action.payload]
                    }
                })
                break
            case CRUDActions.DELETE:
                setSolverConfiguration(prevState => {
                    if (prevState == undefined) return undefined
                    return {
                        ...solverConfiguration!,
                        employees: solverConfiguration!.employees.filter(a => a.employeeId !== action.payload.employeeId)
                    }
                })
        }
    }

    function handleShiftRequestsChanged(changedRequests: Omit<RequestedShiftConstraintDTO, "id">[]) {
        setRequestedShiftConstraints(prevState => {
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
        validationCtx.validate({...solverConfiguration!, constraints: combineConstraints()})
    }

    function handleStartCalculation() {
        handleStopCalculation()
        setGridDisplayMode(GridDisplayMode.RESULT)
        TicketEndpoint.issueTicket().then(ticket => {
            setResultSubscription(SolverEndpoint.solveSavedProblem(solverConfiguration?.id, ticket)
                .onNext(value => {
                    if (value.solutionStatus !== SolutionStatus.OK) {
                        Notification.show(value.message, {
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
                    Notification.show("Chyba!", {
                        position: "top-center",
                        duration: 5000,
                        theme: "error"
                    })
                    handleStopCalculation()
                })
            )
        })
    }

    function handleResultSelectionChanged(offset: 1 | -1) {
        setResultCache(prevState => ({
            ...prevState,
            selectedIndex: Math.min(RESULT_CACHE_SIZE - 1, Math.max(0, prevState.selectedIndex + offset))
        }))
    }

    function handleTripleShiftConstraintAction(action: CrudAction<TripleShiftConstraintDTO>) {
        setTripleShiftConstraintRequests(prevState => updateList(action, prevState))
    }

    function handleEmployeePerShiftAction(action: CrudAction<EmployeesPerShiftConstraintDTO>) {
        setEmployeesPerShiftRequests(prevState => updateList(action, prevState))
    }

    function handleShiftFollowupRestrictionAction(action: CrudAction<ShiftFollowupRestrictionConstraintDTO>) {
        setShiftFollowupRestrictionRequests(prevState => updateList(action, prevState))
    }

    function handleConsecutiveWorkingDaysAction(action: CrudAction<ConsecutiveWorkingDaysConstraintDTO>) {
        setConsecutiveWorkingDaysRequests(prevState => updateList(action, prevState))
    }

    function handleShiftPerScheduleAction(action: CrudAction<ShiftsPerScheduleConstraintDTO>) {
        setShiftPerScheduleRequests(prevState => updateList(action, prevState))
    }

    function handleWeekendRequestAction(action: CrudAction<WeekendConstraintDTO>) {
        setWeekendRequests(prevState => updateList(action, prevState))
    }

    function handleEvenDistributionRequestAction(action: CrudAction<EvenShiftDistributionConstraintDTO>) {
        setEvenDistributionRequests(prevState => updateList(action, prevState))
    }

    function handleTeamAssignmentAction(action: CrudAction<TeamAssignmentConstraintDTO>) {
        function updateTeamAssignments(action: CrudAction<TeamAssignmentConstraintDTO>, requests: TeamAssignmentConstraintDTO[]) {
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
                        value={solverConfiguration?.name}
                        onChange={e => setSolverConfiguration({...solverConfiguration!, name: e.target.value})}
                        readonly={modeCtx.mode !== ScheduleMode.EDIT}
                        disabled={!solverConfiguration}
                        style={{width: 385}}
                    />
                    <DatePicker
                        label={"Od"}
                        value={solverConfiguration && solverConfiguration?.startDate}
                        onChange={e => setSolverConfiguration({
                            ...solverConfiguration!,
                            startDate: e.target.value
                        })}
                        readonly={modeCtx.mode !== ScheduleMode.EDIT}
                        disabled={!solverConfiguration}
                    />
                    <DatePicker
                        label={"Do"}
                        value={solverConfiguration && solverConfiguration?.endDate}
                        onChange={e => setSolverConfiguration({
                            ...solverConfiguration!,
                            endDate: e.target.value
                        })}
                        readonly={modeCtx.mode !== ScheduleMode.EDIT}
                        disabled={!solverConfiguration}
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
                request={solverConfiguration}
                onConfigSelected={handleFetchConfig}
                onValidateRequest={validateRequest}
                resultCache={resultCache}
                onSave={handleSave}
                onUpdate={handleUpdate}
                onCancel={handleCancel}
                onClearCache={() => setResultCache({results: [], selectedIndex: 0})}
                onExportToExcel={() => exportToExcel(solverConfiguration!.name, solverConfiguration!.employees, employees, resultCache.results[resultCache.selectedIndex])}
                cacheSize={RESULT_CACHE_SIZE}
                onResultSelectionChanged={handleResultSelectionChanged}
                gridDisplayMode={gridDisplayMode}
                onGridDisplayModeChange={setGridDisplayMode}
            />
            {solverConfiguration ? renderGridHeader() : <h2 style={{marginTop: 30, padding: 10}}>Vyberte rozvrh</h2>}
            {solverConfiguration &&
                <Fragment>
                    <ScheduleSettingsDialog
                        isOpen={isScheduleConfigDialogOpen}
                        onOpenChanged={setIsScheduleConfigDialogOpen}
                        request={solverConfiguration}
                        employees={employees}
                        onAssignmentAction={handleAssignmentAction}
                        employeesPerShift={employeesPerShiftRequests}
                        onEmployeePerShiftAction={handleEmployeePerShiftAction}
                        shiftFollowupRestriction={shiftFollowupRestrictionRequests}
                        onShiftFollowupRestrictionAction={handleShiftFollowupRestrictionAction}
                        consecutiveWorkingDays={consecutiveWorkingDaysRequests}
                        onConsecutiveWorkingDaysAction={handleConsecutiveWorkingDaysAction}
                    />
                    <EmployeeConstraintsDialog
                        key={employeeConfigDialog.selectedEmployeeId}
                        assignment={solverConfiguration.employees.find(w => w.employeeId === employeeConfigDialog.selectedEmployeeId)!}
                        employee={employees.find(e => e.id === employeeConfigDialog.selectedEmployeeId)!}
                        isOpen={employeeConfigDialog.isOpen}
                        onShiftPerScheduleAction={handleShiftPerScheduleAction}
                        shiftsPerScheduleRequests={shiftPerScheduleRequests.filter(r => r.owner === employeeConfigDialog.selectedEmployeeId)}
                        onOpenChanged={(newValue) => setEmployeeConfigDialog(prevState => ({
                            ...prevState,
                            isOpen: newValue
                        }))}
                        shiftPatternRequests={shiftPatternRequests.filter(r => r.owner === employeeConfigDialog.selectedEmployeeId)}
                        onShiftPatternRequestsAction={handleShiftPatternAction}
                        tripleShiftConstraintRequest={tripleShiftConstraintRequests.filter(r => r.owner === employeeConfigDialog.selectedEmployeeId)}
                        onTripleShiftConstraintAction={handleTripleShiftConstraintAction}
                        teamAssignmentRequests={teamAssignmentRequests.filter(r => r.owner === employeeConfigDialog.selectedEmployeeId)}
                        onTeamAssignmentRequestAction={handleTeamAssignmentAction}
                        onAssignmentAction={handleAssignmentAction}
                        weekendRequests={weekendRequests.filter(r => r.owner === employeeConfigDialog.selectedEmployeeId)}
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
                            request={solverConfiguration}
                            employees={employees}
                            shiftRequests={requestedShiftConstraints}
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

function updateAssignmentsWithIndexIntegrity(updated: EmployeeAssignmentDTO, previousState: EmployeeAssignmentDTO[]) {
    const original = previousState.find(a => a.employeeId === updated.employeeId)
    if (!original) {
        return [...previousState, updated]
            .sort((a, b) => a.index - b.index)
            .map((value, index) => ({...value, index}))
    }
    const neighbor = previousState.find(a => {
        return a.employeeId != updated.employeeId && a.index === updated.index
    })
    if (!neighbor) {
        return previousState
            .map(a => {
                if (a.employeeId === updated.employeeId) {
                    return updated
                }
                return a
            }).sort((a, b) => a.index - b.index)
            .map((value, index) => ({...value, index}))
    }
    const offset = updated.index - original.index
    return previousState.map(a => {
        if (a.employeeId === updated.employeeId) {
            return updated
        }
        if (a.employeeId === neighbor.employeeId) {
            return {...neighbor, index: neighbor.index - offset}
        }
        return a
    }).sort((a, b) => a.index - b.index)
        .map((value, index) => ({...value, index}));
}