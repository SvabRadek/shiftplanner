import { DatePicker } from "@hilla/react-components/DatePicker";
import { Fragment, useCallback, useContext, useEffect, useState } from "react";
import {
  ConstraintEndpoint,
  EmployeeEndpoint,
  PlannerConfigurationEndpoint,
  PlannerEndpoint
} from "Frontend/generated/endpoints";
import { Button } from "@hilla/react-components/Button";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { TextField } from "@hilla/react-components/TextField";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { ScheduleGridContainer } from "./components/schedulegrid/ScheduleGridContainer";
import {
  EmployeeRequestConfigDialog
} from "Frontend/views/schedule/components/employeeSettings/EmployeeRequestConfigDialog";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import SpecificShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/SpecificShiftRequestDTO";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import WorkerId from "Frontend/generated/com/cocroachden/planner/lib/WorkerId";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/lib/ConstraintType";
import {
  areShiftRequestsSame,
  CrudAction,
  CRUDActions,
  generateUUID,
  localeDateToStupidDate,
  stupidDateToLocaleDate
} from "Frontend/util/utils";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { Notification } from "@hilla/react-components/Notification";
import { Card } from "Frontend/components/Card";
import { Icon } from "@hilla/react-components/Icon";
import { Subscription } from "@hilla/frontend";
import ScheduleResultDTO from "Frontend/generated/com/cocroachden/planner/solver/ScheduleResultDTO";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ConsecutiveWorkingDaysRequestDTO";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftRequestDTO";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionRequestDTO";
import ShiftPatternRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/ShiftPatternRequestDTO";
import { ScheduleSettingsDialog } from "Frontend/views/schedule/components/schedulesettings/ScheduleSettingsDialog";
import ValidatorResult from "Frontend/generated/com/cocroachden/planner/solver/constraints/validator/ValidatorResult";
import IssueSeverity from "Frontend/generated/com/cocroachden/planner/solver/constraints/validator/IssueSeverity";
import PlannerConfigurationMetaDataDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationMetaDataDTO";
import { exportToExcel } from "Frontend/util/excel";
import SolutionStatus from "Frontend/generated/com/cocroachden/planner/solver/SolutionStatus";
import { ResultSubHeaderStrip } from "Frontend/views/schedule/ResultSubHeaderStrip";
import { HeaderStrip } from "Frontend/views/schedule/HeaderStrip";

type EmployeeConfigDialogParams = {
  isOpen: boolean,
  selectedEmployee?: WorkerId
}

export type ResultCache = {
  results: ScheduleResultDTO[]
  selectedIndex: number
}

function handleUnload(e: Event) {
  e.preventDefault()
}

const RESULT_CACHE_SIZE = 5

export default function ScheduleView() {

  const modeCtx = useContext(ScheduleModeCtx);
  const [employees, setEmployees] = useState<EmployeeRecord[]>([])
  const [configMetaData, setConfigMetaData] = useState<PlannerConfigurationMetaDataDTO[]>([]);
  const [employeeConfigDialog, setEmployeeConfigDialog] = useState<EmployeeConfigDialogParams>({ isOpen: false })
  const [isScheduleConfigDialogOpen, setIsScheduleConfigDialogOpen] = useState(false);
  const [resultCache, setResultCache] = useState<ResultCache>({
    results: [],
    selectedIndex: 0
  });
  const [validatorResult, setValidatorResult] = useState<ValidatorResult | undefined>(undefined);

  const [resultSubscription, setResultSubscription] = useState<Subscription<ScheduleResultDTO> | undefined>();
  const [request, setRequest] = useState<PlannerConfigurationDTO | undefined>();
  const [shiftRequests, setShiftRequests] = useState<SpecificShiftRequestDTO[]>([])
  const [shiftPerScheduleRequests, setShiftPerScheduleRequests] = useState<ShiftsPerScheduleRequestDTO[]>([])
  const [consecutiveWorkingDaysRequests, setConsecutiveWorkingDaysRequests] = useState<ConsecutiveWorkingDaysRequestDTO[]>([]);
  const [employeesPerShiftRequests, setEmployeesPerShiftRequests] = useState<EmployeesPerShiftRequestDTO[]>([]);
  const [shiftFollowupRestrictionRequests, setShiftFollowupRestrictionRequests] = useState<ShiftFollowupRestrictionRequestDTO[]>([]);
  const [shiftPatternRequests, setShiftPatternRequests] = useState<ShiftPatternRequestDTO[]>([]);

  useEffect(() => {
    EmployeeEndpoint.getAllEmployees().then(setEmployees)
    PlannerConfigurationEndpoint.getMetaData().then(setConfigMetaData)
    window.addEventListener("beforeunload", handleUnload)
    return () => {
      window.removeEventListener("beforeunload", handleUnload)
    }
  }, []);

  useEffect(() => {
    if (validatorResult) {
      if (validatorResult?.issues.filter(i => i.severity === IssueSeverity.ERROR).length > 0) {
        Notification.show("V konfiguraci jsou konflikty!", {
          position: "top-center",
          duration: 5000,
          theme: "error"
        })
      }
      if (validatorResult?.issues.filter(i => i.severity === IssueSeverity.WARNING).length > 0) {
        Notification.show("Konfigurace obsahuje varování!", {
          position: "top-center",
          duration: 5000,
          theme: "warning"
        })
      }
    }
  }, [validatorResult]);

  const handleShiftPatternAction = useCallback((action: CrudAction<ShiftPatternRequestDTO>) => {
    switch (action.type) {
      case CRUDActions.CREATE:
        setShiftPatternRequests(prevState => ([
          ...prevState,
          action.payload
        ]))
        break
      case CRUDActions.UPDATE:
        setShiftPatternRequests(prevState => (
          prevState.map(r => {
            if (r.id !== action.payload.id) return r
            return action.payload
          })
        ))
        break
      case CRUDActions.DELETE:
        setShiftPatternRequests(prevState => (
          prevState.filter(r => r.id !== action.payload.id)
        ))
    }
  }, []);


  function handleCancel() {
    handleFetchConfig(request?.id!)
  }

  async function handleSave() {
    await PlannerConfigurationEndpoint.save(
      request!,
      shiftRequests,
      shiftPatternRequests,
      employeesPerShiftRequests,
      shiftFollowupRestrictionRequests,
      shiftPerScheduleRequests,
      consecutiveWorkingDaysRequests
    ).then(response => {
      handleFetchConfig(response)
      Notification.show("Konfigurace úspěšně uložena!", {
        position: "top-center",
        duration: 5000,
        theme: "success"
      })
    })
  }

  async function handleUpdate() {
    await PlannerConfigurationEndpoint.update(
      request!,
      shiftRequests,
      shiftPatternRequests,
      employeesPerShiftRequests,
      shiftFollowupRestrictionRequests,
      shiftPerScheduleRequests,
      consecutiveWorkingDaysRequests
    ).then(response => {
      handleFetchConfig(response)
      Notification.show("Konfigurace úuspěšně upravena!", {
        position: "top-center",
        duration: 5000,
        theme: "success"
      })
    })
  }

  async function handleConfigAction(action: CrudAction<PlannerConfigurationMetaDataDTO>) {
    switch (action.type) {
      case CRUDActions.DELETE:
        await PlannerConfigurationEndpoint.delete(action.payload.id)
        PlannerConfigurationEndpoint.getMetaData().then(setConfigMetaData)
        break
      case CRUDActions.READ:
        handleFetchConfig(action.payload.id)
    }
  }

  function handleFetchConfig(configId: string) {
    PlannerConfigurationEndpoint.getConfiguration(configId).then(configResponse => {
      setRequest(configResponse)
      setValidatorResult(undefined)
      setResultCache({ selectedIndex: 0, results: [] })
      ConstraintEndpoint.findSpecificShiftRequests(
        configResponse.constraintRequestInstances
          .filter(l => l.requestType === ConstraintType.SPECIFIC_SHIFT_REQUEST)
          .map(l => l.requestId)
      ).then(setShiftRequests)
      ConstraintEndpoint.findShiftsPerScheduleRequests(
        configResponse.constraintRequestInstances
          .filter(l => l.requestType === ConstraintType.SHIFT_PER_SCHEDULE)
          .map(l => l.requestId)
      ).then(setShiftPerScheduleRequests)
      ConstraintEndpoint.findConsecutiveWorkingDaysRequests(
        configResponse.constraintRequestInstances
          .filter(l => l.requestType === ConstraintType.CONSECUTIVE_WORKING_DAYS)
          .map(l => l.requestId)
      ).then(setConsecutiveWorkingDaysRequests)
      ConstraintEndpoint.findEmployeesPerShiftRequests(
        configResponse.constraintRequestInstances
          .filter(l => l.requestType === ConstraintType.WORKERS_PER_SHIFT)
          .map(l => l.requestId)
      ).then(setEmployeesPerShiftRequests)
      ConstraintEndpoint.findShiftFollowupRestrictionRequests(
        configResponse.constraintRequestInstances
          .filter(l => l.requestType === ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION)
          .map(l => l.requestId)
      ).then(setShiftFollowupRestrictionRequests)
      ConstraintEndpoint.findShiftPatternRequests(
        configResponse.constraintRequestInstances
          .filter(l => l.requestType === ConstraintType.SHIFT_PATTERN_CONSTRAINT)
          .map(l => l.requestId)
      ).then(setShiftPatternRequests)
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

  function handleShiftRequestsChanged(changedRequests: Omit<SpecificShiftRequestDTO, "id">[]) {
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

  async function validateRequest() {
    const validation = await ConstraintEndpoint.validate(
      request!,
      shiftRequests,
      shiftPatternRequests,
      employeesPerShiftRequests,
      consecutiveWorkingDaysRequests,
      shiftFollowupRestrictionRequests,
      shiftPerScheduleRequests
    )
    setValidatorResult(validation)
    if (validation.issues.length > 0) {
      Notification.show(
        "Nalezeny problémy!",
        { position: "top-center", theme: "error" }
      )
    } else {
      Notification.show(
        "Vše se zdá v pořádku.",
        { position: "top-center", theme: "success" }
      )
    }
    return validation
  }

  function handleStartCalculation() {
    if (resultSubscription) {
      resultSubscription.cancel()
      setResultSubscription(undefined)
    }
    validateRequest().then(validation => {
      if (validation.issues.filter(i => i.severity === IssueSeverity.ERROR).length > 0) {
        return;
      }
      modeCtx.setMode(ScheduleMode.CALCULATING)
      setResultSubscription(PlannerEndpoint.solve(request?.id)
        .onNext(value => {
          if (value.solutionStatus !== SolutionStatus.OK) {
            Notification.show("Neřešitelné zadání!", {
              position: "top-center",
              duration: 5000,
              theme: "error"
            })
            handleStopCalculation()
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
          Notification.show("Ztráta spojení!", {
            position: "top-center",
            duration: 5000,
            theme: "warning"
          })
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

  function handleEmployeePerShiftAction(action: CrudAction<EmployeesPerShiftRequestDTO>) {
    switch (action.type) {
      case CRUDActions.UPDATE:
        setEmployeesPerShiftRequests(prevState =>
          prevState.map(r => {
            if (action.payload.id !== r.id) return r
            return action.payload
          })
        )
        break
      case CRUDActions.DELETE:
        setEmployeesPerShiftRequests(prevState =>
          prevState.filter(r => r.id !== action.payload.id)
        )
        break
      case CRUDActions.CREATE:
        setEmployeesPerShiftRequests(prevState => [...prevState, action.payload])
        break
    }
  }

  function handleShiftFollowupRestrictionAction(action: CrudAction<ShiftFollowupRestrictionRequestDTO>) {
    switch (action.type) {
      case CRUDActions.UPDATE:
        setShiftFollowupRestrictionRequests(prevState =>
          prevState.map(r => {
            if (action.payload.id !== r.id) return r
            return action.payload
          })
        )
        break
      case CRUDActions.DELETE:
        setShiftFollowupRestrictionRequests(prevState =>
          prevState.filter(r => r.id !== action.payload.id)
        )
        break
      case CRUDActions.CREATE:
        setShiftFollowupRestrictionRequests(prevState =>
          [...prevState, action.payload]
        )
    }
  }

  function handleConsecutiveWorkingDaysAction(action: CrudAction<ConsecutiveWorkingDaysRequestDTO>) {
    switch (action.type) {
      case CRUDActions.UPDATE:
        setConsecutiveWorkingDaysRequests(prevState =>
          prevState.map(r => {
            if (action.payload.id !== r.id) return r
            return action.payload
          })
        )
        break
      case CRUDActions.DELETE:
        setConsecutiveWorkingDaysRequests(prevState =>
          prevState.filter(r => r.id !== action.payload.id)
        )
        break
      case CRUDActions.CREATE:
        setConsecutiveWorkingDaysRequests(prevState =>
          [...prevState, action.payload]
        )
    }
  }

  function handleShiftPerScheduleAction(action: CrudAction<ShiftsPerScheduleRequestDTO>) {
    switch (action.type) {
      case CRUDActions.UPDATE:
        setShiftPerScheduleRequests(prevState =>
          prevState.map(r => {
            if (action.payload.id !== r.id) return r
            return action.payload
          })
        )
        break
      case CRUDActions.DELETE:
        setShiftPerScheduleRequests(prevState =>
          prevState.filter(r => r.id !== action.payload.id)
        )
        break
      case CRUDActions.CREATE:
        setShiftPerScheduleRequests(prevState =>
          [...prevState, action.payload]
        )
    }
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
        onConfigAction={handleConfigAction}
        request={request}
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
                      validation={validatorResult}
                  />
              </Card>
          </Fragment>
      }
    </VerticalLayout>
  )
}

