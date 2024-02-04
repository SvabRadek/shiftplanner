import { DatePicker } from "@hilla/react-components/DatePicker";
import { useContext, useEffect, useRef, useState } from "react";
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
import { AddEmployeeDialog } from "Frontend/views/schedule/components/AddEmployeeDialog";
import { EmployeeAction, EmployeeActionEnum } from "Frontend/views/schedule/components/schedulegrid/GridNameCell";
import {
  EmployeeConfigModel,
  EmployeeRequestConfigDialog
} from "Frontend/views/schedule/components/employeeConstraints/EmployeeRequestConfigDialog";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import SpecificShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/SpecificShiftRequestDTO";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import WorkerId from "Frontend/generated/com/cocroachden/planner/lib/WorkerId";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/lib/ConstraintType";
import { areShiftRequestsSame, localeDateToStupidDate, stupidDateToLocaleDate } from "Frontend/util/utils";
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
import {
  ScheduleConstraintDialogModel,
  ScheduleConstraintsDialog
} from "Frontend/views/schedule/components/scheduleConstraints/ScheduleConstraintsDialog";

async function saveSpecificShiftRequests(requests: SpecificShiftRequestDTO[]): Promise<string[]> {
  return ConstraintEndpoint.saveAllSpecificShiftRequests(requests)
}

async function saveShiftPerScheduleRequests(requests: ShiftsPerScheduleRequestDTO[]): Promise<string[]> {
  return ConstraintEndpoint.saveAllShiftsPerScheduleRequests(requests)
}

async function saveConsecutiveWorkingDaysRequests(requests: ConsecutiveWorkingDaysRequestDTO[]): Promise<string[]> {
  return ConstraintEndpoint.saveAllConsecutiveWorkingDaysRequests(requests)
}

type EmployeeConfigDialogParams = {
  isOpen: boolean,
  selectedEmployee?: WorkerId
}

export default function ScheduleView() {

  const modeCtx = useContext(ScheduleModeCtx);
  const isCopy = useRef(false);
  const [employees, setEmployees] = useState<EmployeeRecord[]>([])
  const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false)
  const [employeeConfigDialog, setEmployeeConfigDialog] = useState<EmployeeConfigDialogParams>({ isOpen: false })
  const [isScheduleConfigDialogOpen, setIsScheduleConfigDialogOpen] = useState(false);
  const [result, setResult] = useState<ScheduleResultDTO | undefined>();
  const [resultSubscription, setResultSubscription] = useState<Subscription<ScheduleResultDTO> | undefined>();

  const [request, setRequest] = useState<PlannerConfigurationDTO | undefined>();
  const [shiftRequests, setShiftRequests] = useState<SpecificShiftRequestDTO[]>([])
  const [shiftPerScheduleRequests, setShiftPerScheduleRequests] = useState<ShiftsPerScheduleRequestDTO[]>([])
  const [consecutiveWorkingDaysRequests, setConsecutiveWorkingDaysRequests] = useState<ConsecutiveWorkingDaysRequestDTO[]>([]);

  useEffect(() => {
    EmployeeService.getAllEmployees().then(setEmployees)
    window.addEventListener("beforeunload", handleUnload)
    return () => {
      window.removeEventListener("beforeunload", handleUnload)
    }
  }, []);

  function handleUnload(e: Event) {
    e.preventDefault()
  }

  function handleCancel() {
    isCopy.current = false
    handleConfigSelected(request?.id!)
  }

  async function handleSave() {
    const [specificShiftIds, shiftPerScheduleIds, consecutiveWorkingDaysIds] = await Promise.all([
      saveSpecificShiftRequests(shiftRequests),
      saveShiftPerScheduleRequests(shiftPerScheduleRequests),
      saveConsecutiveWorkingDaysRequests(consecutiveWorkingDaysRequests)
    ])
    await PlannerConfigurationEndpoint.save({
      ...request!,
      constraintRequestInstances: [
        ...specificShiftIds.map(id => ({ requestType: ConstraintType.SPECIFIC_SHIFT_REQUEST, requestId: id })),
        ...shiftPerScheduleIds.map(id => ({ requestType: ConstraintType.SHIFT_PER_SCHEDULE, requestId: id })),
        ...consecutiveWorkingDaysIds.map(id => ({
          requestType: ConstraintType.CONSECUTIVE_WORKING_DAYS,
          requestId: id
        }))
      ]
    }).then(response => {
      handleConfigSelected(response)
      Notification.show("Konfigurace uspesne ulozena!", {
        position: "top-center",
        duration: 5000,
        theme: "success"
      })
    })
  }

  function handleConfigSelected(configId: string) {
    PlannerConfigurationEndpoint.getConfiguration(configId).then(configResponse => {
      setRequest(configResponse)
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
    })
    modeCtx.setMode(ScheduleMode.READONLY)
  }

  function handleAddEmployee(employee: EmployeeRecord) {
    setRequest(prevState => {
      if (!prevState) return undefined
      return {
        ...prevState,
        workers: [...prevState.workers, { workerId: employee.workerId }]
      }
    })
  }

  function handleEmployeeConfigSave(config: EmployeeConfigModel) {
    setShiftPerScheduleRequests(prevState => {
      return [
        ...prevState.filter(previous => previous.owner.workerId !== config.workerId),
        ...config.shiftsPerScheduleRequests
      ]
    })
    setEmployeeConfigDialog({ isOpen: false })
  }

  function handleEmployeeAction(action: EmployeeAction) {
    switch (action.type) {
      case EmployeeActionEnum.ADD:
        setIsAddEmployeeDialogOpen(true)
        break
      case EmployeeActionEnum.DELETE:
        setRequest(prevState => {
          if (!prevState) return undefined
          return {
            ...prevState,
            workers: prevState.workers.filter(w => w.workerId !== action.workerId)
          }
        })
        break
      case EmployeeActionEnum.EDIT:
        setEmployeeConfigDialog(() => ({ selectedEmployee: { workerId: action.workerId }, isOpen: true }))
        break
    }
  }

  function handleShiftRequestsChanged(changedRequests: SpecificShiftRequestDTO[]) {
    setShiftRequests(prevState => {
      return [
        ...prevState.filter(r => !changedRequests.some(changed => areShiftRequestsSame(r, changed))),
        ...changedRequests.filter(r => r.requestedShift !== WorkShifts.ANY)
      ]
    })
  }

  function handleCopyConfig() {
    isCopy.current = true
    setRequest(prevState => ({
      ...prevState!,
      name: ""
    }));
    modeCtx.setMode(ScheduleMode.EDIT)
  }

  function handleStopCalculation() {
    if (resultSubscription) {
      PlannerEndpoint.stop()
      resultSubscription.cancel()
      setResultSubscription(() => undefined)
    }
  }

  function handleStartCalculation() {
    if (resultSubscription) {
      handleStopCalculation()
    }
    setTimeout(
      () => {
        if (resultSubscription) {
          handleStopCalculation()
          Notification.show("Casovy limit vyprsel.", {
            position: "top-center",
            duration: 5000,
            theme: "success"
          })
        }
      },
      60 * 1000
    )
    setResultSubscription(PlannerEndpoint.solve(request?.id, 60)
      .onNext(value => {
        setResult(value)
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
  }

  function handleScheduleConstraintSave(value: ScheduleConstraintDialogModel) {
    setConsecutiveWorkingDaysRequests(value.consecutiveWorkingDaysRequests)
    setIsScheduleConfigDialogOpen(false)
  }

  function renderHeaderStrip() {
    const isRequestLoaded = request !== undefined
    return (
      <HorizontalLayout theme={"spacing"}>
        {
          resultSubscription ?
            <Button onClick={handleStopCalculation} theme={"primary"}>
              <Icon icon={"vaadin:stop"}></Icon>
              Stop
            </Button>
            : <Button onClick={handleStartCalculation} disabled={modeCtx.mode === ScheduleMode.EDIT || !request}
                      theme={"primary"}>
              <Icon icon={"vaadin:play"}/>
              Vypocitat
            </Button>
        }
        <ConfigSelectDialog onConfigSelected={value => handleConfigSelected(value.id)}/>
        {isRequestLoaded && <Button disabled={modeCtx.mode === ScheduleMode.EDIT}
                                    onClick={() => modeCtx.setMode(ScheduleMode.EDIT)}>Upravit</Button>}
        {isRequestLoaded &&
            <Button disabled={modeCtx.mode === ScheduleMode.EDIT} onClick={handleCopyConfig}>Zkopirovat</Button>}
        {isRequestLoaded && <Button disabled={modeCtx.mode !== ScheduleMode.EDIT} theme={"secondary"}
                                    onClick={handleSave}>Ulozit</Button>}
        {isRequestLoaded && <Button disabled={modeCtx.mode !== ScheduleMode.EDIT} theme={"secondary"}
                                    onClick={handleCancel}>Zrusit</Button>}
        {result && <Button theme={"secondary"} onClick={() => setResult(undefined)}>Vycistit vysledky</Button>}
      </HorizontalLayout>
    )
  }

  function renderResultStrip() {
    if (!resultSubscription) return null
    return (
      <VerticalLayout style={{ width: "100%" }}>
        {<HorizontalLayout theme={"spacing"}>
          <span>Reseni: {result ? result.resultIndex : "-"}</span>
          <span>Skore: {result ? result.resultScore : "-"}</span>
        </HorizontalLayout>}
        {<ProgressBar indeterminate></ProgressBar>}
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
            style={{ width: "385px" }}
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
            disabled={modeCtx.mode !== ScheduleMode.EDIT}
            theme={"secondary"}
            onClick={() => setIsScheduleConfigDialogOpen(true)}
          >
            <Icon style={{ marginRight: "5px" }} icon={"vaadin:cog"}/>
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
        {renderResultStrip()}
      </Card>
      {request ? renderGridHeader() : <h2 style={{ marginTop: "30px", padding: "10px" }}>Vyberte rozvrh</h2>}
      {request &&
          <>
              <ScheduleConstraintsDialog
                  isOpen={isScheduleConfigDialogOpen}
                  onOpenChanged={setIsScheduleConfigDialogOpen}
                  consecutiveWorkingDaysRequests={consecutiveWorkingDaysRequests}
                  onSave={handleScheduleConstraintSave}
              />
              <EmployeeRequestConfigDialog
                  key={employeeConfigDialog.selectedEmployee?.workerId}
                  employee={employees.find(w => w.workerId === employeeConfigDialog.selectedEmployee?.workerId)!}
                  isOpen={employeeConfigDialog.isOpen}
                  onOpenChanged={(newValue) => setEmployeeConfigDialog(prevState => ({
                    ...prevState,
                    isOpen: newValue
                  }))}
                  shiftsPerScheduleRequests={shiftPerScheduleRequests.filter(r => r.owner.workerId === employeeConfigDialog.selectedEmployee?.workerId)}
                  onSave={handleEmployeeConfigSave}
                  readonly={modeCtx.mode !== ScheduleMode.EDIT}
              />
              <AddEmployeeDialog
                  employees={employees}
                  selectedWorkers={request.workers}
                  onEmployeeSelected={handleAddEmployee}
                  onOpenChanged={setIsAddEmployeeDialogOpen}
                  isOpen={isAddEmployeeDialogOpen}
              />
              <Card
                  style={{
                    maxWidth: "100%",
                    borderWidth: "1px",
                    borderColor: "var(--lumo-shade-70pct)"
                  }}
              >
                  <ScheduleGridContainer
                      request={request}
                      employees={employees}
                      shiftRequests={shiftRequests}
                      shiftPerScheduleRequests={shiftPerScheduleRequests}
                      onEmployeeAction={handleEmployeeAction}
                      onShiftRequestsChanged={handleShiftRequestsChanged}
                      result={result}
                  />
              </Card>
          </>
      }
      <HorizontalLayout theme={"spacing"}>
      </HorizontalLayout>
    </VerticalLayout>
  )
}

