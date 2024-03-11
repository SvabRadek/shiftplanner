import { DatePicker } from "@hilla/react-components/DatePicker";
import { useCallback, useContext, useEffect, useRef, useState } from "react";
import {
  ConstraintEndpoint,
  EmployeeService,
  PlannerConfigurationEndpoint,
  PlannerEndpoint
} from "Frontend/generated/endpoints";
import { Button } from "@hilla/react-components/Button";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { TextField } from "@hilla/react-components/TextField";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { ConfigSelectDialog } from "Frontend/views/schedule/components/ConfigSelectDialog";
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
import { ProgressBar } from "@hilla/react-components/ProgressBar.js";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import ConsecutiveWorkingDaysRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ConsecutiveWorkingDaysRequestDTO";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftRequestDTO";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionRequestDTO";
import ShiftPatternRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/ShiftPatternRequestDTO";
import { ScheduleSettingsDialog } from "Frontend/views/schedule/components/schedulesettings/ScheduleSettingsDialog";
import { StopWatch } from "Frontend/components/StopWatch";
import ValidatorResult from "Frontend/generated/com/cocroachden/planner/solver/constraints/validator/ValidatorResult";
import IssueSeverity from "Frontend/generated/com/cocroachden/planner/solver/constraints/validator/IssueSeverity";
import PlannerConfigurationMetaDataDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationMetaDataDTO";
import { exportToExcel } from "Frontend/util/excel";
import SolutionStatus from "Frontend/generated/com/cocroachden/planner/solver/SolutionStatus";

type EmployeeConfigDialogParams = {
  isOpen: boolean,
  selectedEmployee?: WorkerId
}

type ResultCache = {
  results: ScheduleResultDTO[]
  selectedIndex: number
}

function handleUnload(e: Event) {
  e.preventDefault()
}

const RESULT_CACHE_SIZE = 5

export default function ScheduleView() {

  const isConfigNew = useRef(false);
  const modeCtx = useContext(ScheduleModeCtx);
  const [employees, setEmployees] = useState<EmployeeRecord[]>([])
  const [configMetaData, setConfigMetaData] = useState<PlannerConfigurationMetaDataDTO[]>([]);
  const [employeeConfigDialog, setEmployeeConfigDialog] = useState<EmployeeConfigDialogParams>({ isOpen: false })
  const [isScheduleConfigDialogOpen, setIsScheduleConfigDialogOpen] = useState(false);
  const [isConfigSelectDialogOpen, setIsConfigSelectDialogOpen] = useState(false);
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
    EmployeeService.getAllEmployees().then(setEmployees)
    PlannerConfigurationEndpoint.getMetaData().then(setConfigMetaData)
    window.addEventListener("beforeunload", handleUnload)
    return () => {
      window.removeEventListener("beforeunload", handleUnload)
    }
  }, []);

  useEffect(() => {
    if (isConfigSelectDialogOpen) {
      PlannerConfigurationEndpoint.getMetaData().then(setConfigMetaData)
    }
  }, [isConfigSelectDialogOpen]);

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
        Notification.show("Konfigurace obsahuje varovani!", {
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
    isConfigNew.current = false
    handleFetchConfig(request?.id!)
  }

  async function handleSave() {
    if (isConfigNew) {
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
        Notification.show("Konfigurace uspesne ulozena!", {
          position: "top-center",
          duration: 5000,
          theme: "success"
        })
      })
    } else {
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
        Notification.show("Konfigurace uspesne upravena!", {
          position: "top-center",
          duration: 5000,
          theme: "success"
        })
      })
    }
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
    isConfigNew.current = false
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

  function handleCopyConfig() {
    isConfigNew.current = true
    setRequest(prevState => ({
      ...prevState!,
      name: ""
    }));
    modeCtx.setMode(ScheduleMode.EDIT)
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
            Notification.show("Neresitelne zadani!", {
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
          Notification.show("Vypocet uspesne ukoncen!", {
            position: "top-center",
            duration: 5000,
            theme: "success"
          })
          setResultSubscription(undefined)
        }).onError(() => {
          Notification.show("Ztrata spojeni!", {
            position: "top-center",
            duration: 5000,
            theme: "warning"
          })
        })
      )
    })
  }

  function handleResultSelect(offset: 1 | -1) {
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

  function renderHeaderStrip() {
    const isRequestLoaded = request !== undefined
    return (
      <HorizontalLayout theme={"spacing"}>
        {resultSubscription ?
          <Button onClick={handleStopCalculation} theme={"primary"}>
            <Icon icon={"vaadin:stop"}></Icon>
            Stop
          </Button>
          : <Button onClick={handleStartCalculation}
                    disabled={modeCtx.mode === ScheduleMode.EDIT || !request}
                    theme={"primary"}>
            <Icon icon={"vaadin:play"}/>
            Vypocitat
          </Button>
        }
        <Button theme={"icon primary"} onClick={() => setIsConfigSelectDialogOpen(true)}>
          <Icon icon={"vaadin:cog"} slot={"prefix"}/>
          Vybrat konfiguraci
        </Button>
        <ConfigSelectDialog
          configMetaData={configMetaData}
          onOpenChanged={value => setIsConfigSelectDialogOpen(value)}
          isOpen={isConfigSelectDialogOpen}
          onConfigAction={handleConfigAction}
        />
        {isRequestLoaded && <Button
            disabled={modeCtx.mode === ScheduleMode.EDIT || modeCtx.mode === ScheduleMode.CALCULATING}
            onClick={() => modeCtx.setMode(ScheduleMode.EDIT)}>
            Upravit</Button>
        }
        {isRequestLoaded && <Button
            disabled={modeCtx.mode === ScheduleMode.EDIT || modeCtx.mode === ScheduleMode.CALCULATING}
            onClick={handleCopyConfig}>
            Zkopirovat</Button>
        }
        {isRequestLoaded && <Button
            disabled={modeCtx.mode === ScheduleMode.CALCULATING}
            onClick={validateRequest}>
            Zkontrolovat</Button>
        }
        {isRequestLoaded && <Button
            disabled={modeCtx.mode !== ScheduleMode.EDIT}
            theme={"secondary"}
            onClick={handleSave}>Ulozit</Button>
        }
        {isRequestLoaded && <Button
            disabled={modeCtx.mode !== ScheduleMode.EDIT}
            theme={"secondary"}
            onClick={handleCancel}>Zrusit</Button>
        }
        {resultCache.results.length > 0 && !resultSubscription &&
            <Button theme={"secondary"} onClick={() => setResultCache({ results: [], selectedIndex: 0 })}>
                Vycistit vysledky
            </Button>
        }
      </HorizontalLayout>
    )
  }

  function renderResultStrip() {
    return (
      <VerticalLayout style={{ paddingTop: 10, width: "100%" }}>
        <HorizontalLayout theme={"spacing"} style={{ alignItems: "center" }}>
          {!resultSubscription &&
              <Button disabled={resultCache.selectedIndex === 0}
                      onClick={() => handleResultSelect(-1)}
                      theme={"small icon"}>
                  <Icon style={{ transform: "rotate(180deg)" }} icon={"vaadin:play"}/>
              </Button>
          }
          <span
            style={{ userSelect: "none" }}>Reseni: {resultCache.results.length > 0 ? resultCache.results[resultCache.selectedIndex].resultIndex : "-"}
          </span>
          <span
            style={{ userSelect: "none" }}>Skore: {resultCache.results.length > 0 ? resultCache.results[resultCache.selectedIndex].resultScore : "-"}
          </span>
          {!resultSubscription &&
              <Button disabled={resultCache.selectedIndex === RESULT_CACHE_SIZE - 1}
                      onClick={() => handleResultSelect(1)}
                      theme={"small icon"}><Icon icon={"vaadin:play"}/></Button>
          }
          <StopWatch style={{
            borderLeft: "solid",
            paddingLeft: 10,
            borderWidth: 1,
            borderColor: "var(--lumo-contrast-20pct)"
          }} isRunning={resultSubscription !== undefined}></StopWatch>
          <Button theme={"small"}
                  onClick={() => exportToExcel(employees, resultCache.results[resultCache.selectedIndex])}>Export</Button>
        </HorizontalLayout>
        {resultSubscription && <ProgressBar style={{ marginBottom: 0 }} indeterminate></ProgressBar>}
      </VerticalLayout>
    )
  }

  function renderGridHeader() {
    return (
      <Card style={{ width: "100%" }}>
        <HorizontalLayout theme={"spacing"} style={{ alignItems: "end" }}>
          <TextField
            label={"Nazev"}
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
            Nastaveni
          </Button>
        </HorizontalLayout>
      </Card>
    )
  }

  return (
    <VerticalLayout theme={"spacing padding"}>
      <Card style={{ width: "100%" }}>
        {renderHeaderStrip()}
        {(resultSubscription || resultCache.results.length > 0) && renderResultStrip()}
      </Card>
      {request ? renderGridHeader() : <h2 style={{ marginTop: 30, padding: 10 }}>Vyberte rozvrh</h2>}
      {request &&
          <>
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
                    maxWidth: "100%",
                    borderWidth: 1,
                    borderColor: "var(--lumo-shade-70pct)"
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
          </>
      }
      <HorizontalLayout theme={"spacing"}>
      </HorizontalLayout>
    </VerticalLayout>
  )
}

