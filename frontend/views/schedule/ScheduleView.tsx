import { DatePicker } from "@hilla/react-components/DatePicker";
import { Fragment, useContext, useEffect, useState } from "react";
import { EmployeeEndpoint, SolverConfigurationEndpoint, SolverEndpoint } from "Frontend/generated/endpoints";
import { Button } from "@hilla/react-components/Button";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { TextField } from "@hilla/react-components/TextField";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { ScheduleGridContainer } from "./components/schedulegrid/ScheduleGridContainer";
import {
  EmployeeRequestConfigDialog
} from "Frontend/views/schedule/components/employeesettings/EmployeeRequestConfigDialog";
import {
  areShiftRequestsSame,
  CrudAction,
  CRUDActions,
  generateUUID,
  localeDateToStupidDate,
  stupidDateToLocaleDate
} from "Frontend/util/utils";
import { Notification } from "@hilla/react-components/Notification";
import { Card } from "Frontend/components/Card";
import { Icon } from "@hilla/react-components/Icon";
import { Subscription } from "@hilla/frontend";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ConsecutiveWorkingDaysRequestDTO";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeesPerShiftRequestDTO";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftFollowupRestrictionRequestDTO";
import ShiftPatternRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftPatternRequestDTO";
import { ScheduleSettingsDialog } from "Frontend/views/schedule/components/schedulesettings/ScheduleSettingsDialog";
import { exportToExcel } from "Frontend/util/excel";
import { HeaderStrip } from "Frontend/views/schedule/HeaderStrip";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftsPerScheduleRequestDTO";
import ConstraintRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintRequestDTO";
import { ValidationContext } from "Frontend/views/schedule/components/validation/ScheduleValidationCtxProvider";
import TripleShiftConstraintRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/api/TripleShiftConstraintRequestDTO";
import SolverConfigurationDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverConfigurationDTO";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/repository/EmployeeRecord";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverSolutionDTO";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintType";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";
import SolutionStatus from "Frontend/generated/com/cocroachden/planner/solver/api/SolutionStatus";
import EmployeeShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeeShiftRequestDTO";
import EmployeeId from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeId";

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
  const [employees, setEmployees] = useState<EmployeeRecord[]>([])
  const [employeeConfigDialog, setEmployeeConfigDialog] = useState<EmployeeConfigDialogParams>({ isOpen: false })
  const [isScheduleConfigDialogOpen, setIsScheduleConfigDialogOpen] = useState(false);
  const [resultCache, setResultCache] = useState<ResultCache>({ results: [], selectedIndex: 0 });
  const [resultSubscription, setResultSubscription] = useState<Subscription<SolverSolutionDTO> | undefined>();

  const [request, setRequest] = useState<SolverConfigurationDTO | undefined>();
  const [shiftRequests, setShiftRequests] = useState<EmployeeShiftRequestDTO[]>([])
  const [shiftPerScheduleRequests, setShiftPerScheduleRequests] = useState<ShiftsPerScheduleRequestDTO[]>([])
  const [consecutiveWorkingDaysRequests, setConsecutiveWorkingDaysRequests] = useState<ConsecutiveWorkingDaysRequestDTO[]>([]);
  const [employeesPerShiftRequests, setEmployeesPerShiftRequests] = useState<EmployeesPerShiftRequestDTO[]>([]);
  const [shiftFollowupRestrictionRequests, setShiftFollowupRestrictionRequests] = useState<ShiftFollowupRestrictionRequestDTO[]>([]);
  const [shiftPatternRequests, setShiftPatternRequests] = useState<ShiftPatternRequestDTO[]>([]);
  const [tripleShiftConstraintRequests, setTripleShiftConstraintRequests] = useState<TripleShiftConstraintRequestDTO[]>([]);

  useEffect(() => {
    EmployeeEndpoint.getAllEmployees().then(setEmployees)
    window.addEventListener("beforeunload", handleUnload)
    return () => {
      window.removeEventListener("beforeunload", handleUnload)
    }
  }, []);

  function handleShiftPatternAction(action: CrudAction<ShiftPatternRequestDTO>) {
    updateList(action, shiftPatternRequests)
  }

  function handleCancel() {
    handleFetchConfig(request?.id!)
  }

  async function handleSave() {
    const combinedList = [
      ...shiftRequests,
      ...shiftPatternRequests,
      ...employeesPerShiftRequests,
      ...shiftFollowupRestrictionRequests,
      ...shiftPerScheduleRequests,
      ...consecutiveWorkingDaysRequests,
      ...tripleShiftConstraintRequests
    ] as ConstraintRequestDTO[]
    await SolverConfigurationEndpoint.save({
      ...request!,
      id: generateUUID(),
      constraints: combinedList
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
    const combinedList = [
      ...shiftRequests,
      ...shiftPatternRequests,
      ...employeesPerShiftRequests,
      ...shiftFollowupRestrictionRequests,
      ...shiftPerScheduleRequests,
      ...consecutiveWorkingDaysRequests,
      ...tripleShiftConstraintRequests
    ] as ConstraintRequestDTO[]
    await SolverConfigurationEndpoint.save({
      ...request!,
      constraints: combinedList
    }).then(response => {
      handleFetchConfig(response)
      Notification.show("Konfigurace úuspěšně upravena!", {
        position: "top-center",
        duration: 5000,
        theme: "success"
      })
    })
  }

  function handleFetchConfig(configId: string) {
    SolverConfigurationEndpoint.getConfiguration(configId).then(configResponse => {
      validationCtx.clear()
      setResultCache({ selectedIndex: 0, results: [] })
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
      configResponse["constraints"] = []
      setRequest(configResponse)
    })
    modeCtx.setMode(ScheduleMode.READONLY)
  }

  function handleEmployeeAction(action: CrudAction<EmployeeRecord>) {
    switch (action.type) {
      case CRUDActions.CREATE:
        setRequest(prevState => {
          if (!prevState) return undefined
          return {
            ...prevState,
            workers: [...prevState.workers, { id: action.payload.id }]
          }
        })
        break
      case CRUDActions.DELETE:
        setRequest(prevState => ({
          ...prevState!,
          workers: prevState!.workers.filter(w => w.id !== action.payload.id)
        }))
        break
      case CRUDActions.UPDATE:
        setEmployeeConfigDialog(() =>
          ({ selectedEmployee: { id: action.payload.id }, isOpen: true })
        )
        break
      case CRUDActions.READ:
        setEmployeeConfigDialog({
          selectedEmployee: { id: action.payload.id },
          isOpen: true
        })
        break
    }
  }

  function handleShiftRequestsChanged(changedRequests: Omit<EmployeeShiftRequestDTO, "id">[]) {
    setShiftRequests(prevState => {
      return [
        ...prevState.filter(r => !changedRequests.some(changed => areShiftRequestsSame(r, changed))),
        ...changedRequests.filter(r => r.requestedShift !== WorkShifts.ANY).map(r => ({ ...r, id: generateUUID() }))
      ]
    })
  }

  function handleStopCalculation() {
    modeCtx.setMode(ScheduleMode.READONLY)
    if (resultSubscription) {
      resultSubscription.cancel()
      setResultSubscription(undefined)
    }
  }

  function validateRequest() {
    const combinedList: ConstraintRequestDTO[] = [
      ...shiftRequests,
      ...shiftPatternRequests,
      ...employeesPerShiftRequests,
      ...consecutiveWorkingDaysRequests,
      ...shiftFollowupRestrictionRequests,
      ...shiftPerScheduleRequests,
      ...tripleShiftConstraintRequests
    ]
    validationCtx.validate(request!, combinedList)
  }

  function handleStartCalculation() {
    if (resultSubscription) {
      resultSubscription.cancel()
      setResultSubscription(undefined)
    }
    modeCtx.setMode(ScheduleMode.CALCULATING)
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
        Notification.show("Výpočet úspěšně ukončen!", {
          position: "top-center",
          duration: 5000,
          theme: "success"
        })
        setResultSubscription(undefined)
      }).onError(() => {
        Notification.show("Neřešitelné zadání!", {
          position: "top-center",
          duration: 5000,
          theme: "error"
        })
        setResultSubscription(undefined)
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

  function renderGridHeader() {
    return (
      <Card style={{ width: "100%" }}>
        <HorizontalLayout theme={"spacing"} style={{ alignItems: "end" }}>
          <TextField
            label={"Název"}
            value={request?.name}
            onChange={e => setRequest({ ...request!, name: e.target.value })}
            readonly={modeCtx.mode !== ScheduleMode.EDIT}
            disabled={!request}
            style={{ width: 385 }}
          />
          <DatePicker
            label={"Od"}
            value={request && stupidDateToLocaleDate(request?.startDate)}
            onChange={e => setRequest({
              ...request!,
              startDate: localeDateToStupidDate(e.target.value)
            })}
            readonly={modeCtx.mode !== ScheduleMode.EDIT}
            disabled={!request}
          />
          <DatePicker
            label={"Do"}
            value={request && stupidDateToLocaleDate(request?.endDate)}
            onChange={e => setRequest({
              ...request!,
              endDate: localeDateToStupidDate(e.target.value)
            })}
            readonly={modeCtx.mode !== ScheduleMode.EDIT}
            disabled={!request}
          />
          <Button
            theme={"secondary"}
            onClick={() => setIsScheduleConfigDialogOpen(true)}
          >
            <Icon style={{ marginRight: 5 }} icon={"vaadin:cog"}/>
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
        onClearCache={() => setResultCache({ results: [], selectedIndex: 0 })}
        onExportToExcel={() => exportToExcel(employees, resultCache.results[resultCache.selectedIndex])}
        cacheSize={RESULT_CACHE_SIZE}
        onResultSelectionChanged={handleResultSelectionChanged}
      />
      {request ? renderGridHeader() : <h2 style={{ marginTop: 30, padding: 10 }}>Vyberte rozvrh</h2>}
      {request &&
          <Fragment>
              <ScheduleSettingsDialog
                  isOpen={isScheduleConfigDialogOpen}
                  onOpenChanged={setIsScheduleConfigDialogOpen}
                  request={request}
                  employees={employees}
                  onEmployeeAction={handleEmployeeAction}
                  employeesPerShift={employeesPerShiftRequests}
                  onEmployeePerShiftAction={handleEmployeePerShiftAction}
                  shiftFollowupRestriction={shiftFollowupRestrictionRequests}
                  onShiftFollowupRestrictionAction={handleShiftFollowupRestrictionAction}
                  consecutiveWorkingDays={consecutiveWorkingDaysRequests}
                  onConsecutiveWorkingDaysAction={handleConsecutiveWorkingDaysAction}
              />
              <EmployeeRequestConfigDialog
                  key={employeeConfigDialog.selectedEmployee?.id}
                  employee={employees.find(w => w.id === employeeConfigDialog.selectedEmployee?.id)!}
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
                      employees={employees}
                      shiftRequests={shiftRequests}
                      shiftPatterns={shiftPatternRequests}
                      shiftPerScheduleRequests={shiftPerScheduleRequests}
                      onEmployeeAction={handleEmployeeAction}
                      onShiftRequestsChanged={handleShiftRequestsChanged}
                      result={resultCache.results.length > 0 ? resultCache.results[resultCache.selectedIndex] : undefined}
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
