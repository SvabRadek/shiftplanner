import { DatePicker } from "@hilla/react-components/DatePicker";
import { useEffect, useState } from "react";
import { ConstraintEndpoint, EmployeeService, PlannerConfigurationEndpoint } from "Frontend/generated/endpoints";
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
import {
  areShiftRequestsSame,
  dateToStupidDate,
  fieldDateToStupidDate,
  stupidDateToDateFieldString
} from "Frontend/util/utils";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { Notification } from "@hilla/react-components/Notification";
import { Card } from "Frontend/components/Card";

async function saveSpecificShiftRequests(requests: SpecificShiftRequestDTO[]): Promise<string[]> {
  return ConstraintEndpoint.saveAllSpecificShiftRequests(requests)
}

async function saveShiftPerScheduleRequests(requests: ShiftsPerScheduleRequestDTO[]): Promise<string[]> {
  return ConstraintEndpoint.saveAllShiftsPerScheduleRequests(requests)
}

type EmployeeConfigDialogParams = {
  isOpen: boolean,
  selectedEmployee?: WorkerId
}

export default function ScheduleView() {

  const [request, setRequest] = useState<PlannerConfigurationDTO | undefined>()
  const [employees, setEmployees] = useState<EmployeeRecord[]>([])
  const [shiftRequests, setShiftRequests] = useState<SpecificShiftRequestDTO[]>([])
  const [shiftPerScheduleRequests, setShiftPerScheduleRequests] = useState<ShiftsPerScheduleRequestDTO[]>([])
  const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false)
  const [employeeConfigDialog, setEmployeeConfigDialog] = useState<EmployeeConfigDialogParams>({ isOpen: false })
  const [isInEditMode, setIsInEditMode] = useState<boolean>(false)

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

  async function handleSave() {
    const [specificShiftIds, shiftPerScheduleIds] = await Promise.all([
      saveSpecificShiftRequests(shiftRequests),
      saveShiftPerScheduleRequests(shiftPerScheduleRequests)
    ])
    await PlannerConfigurationEndpoint.save({
      ...request!,
      constraintRequestInstances: [
        ...specificShiftIds.map(id => ({ requestType: ConstraintType.SPECIFIC_SHIFT_REQUEST, requestId: id })),
        ...shiftPerScheduleIds.map(id => ({ requestType: ConstraintType.SHIFT_PER_SCHEDULE, requestId: id }))
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
    })
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

  function renderHeaderStrip(isRequestLoaded: boolean, isInEditMode: boolean) {
    if (isInEditMode) {
      return (
        <HorizontalLayout theme={"spacing"}>
          <ConfigSelectDialog onConfigSelected={value => handleConfigSelected(value.id)}/>
          {
            isRequestLoaded ?
              <Button theme={"primary"} onClick={handleSave}>Uloz</Button> : null
          }
        </HorizontalLayout>
      )
    } else {
      return (
        <HorizontalLayout theme={"spacing"}>
          <ConfigSelectDialog onConfigSelected={value => handleConfigSelected(value.id)}/>
          {
            isRequestLoaded ?
              <>
                <Button onClick={() => setIsInEditMode(true)}>Uprav</Button>
                <Button>Zkopiruj</Button>
              </> : null
          }
        </HorizontalLayout>
      )
    }
  }

  return (
    <VerticalLayout theme={"spacing padding"}>
      <Card style={{ width: "100%" }}>
        {renderHeaderStrip(request !== undefined, isInEditMode)}
      </Card>
      {/*<HorizontalLayout theme={"spacing"}>*/}
      {/*  <ConfigSelectDialog onConfigSelected={value => handleConfigSelected(value.id)}/>*/}
      {/*  {request && !isInEditMode ?*/}
      {/*    <Button*/}
      {/*      onClick={() => setIsInEditMode(true)}*/}
      {/*      theme={"primary"}*/}
      {/*    >*/}
      {/*      Edit*/}
      {/*    </Button>*/}
      {/*    :*/}
      {/*    <Button*/}
      {/*      theme={"primary"}*/}
      {/*      onClick={handleSave}*/}
      {/*    >*/}
      {/*      Uloz*/}
      {/*    </Button>*/}
      {/*  }*/}
      {/*</HorizontalLayout>*/}
      <Card style={{ width: "100%" }}>
        <HorizontalLayout theme={"spacing"}>
          <TextField
            label={"Nazev"}
            value={request?.name}
            onChange={e => setRequest({ ...request!, name: e.target.value })}
            readonly={!isInEditMode}
            disabled={!request}
          />
          <DatePicker
            label={"Od"}
            value={request && stupidDateToDateFieldString(request?.startDate)}
            onChange={e => setRequest({
              ...request!,
              startDate: fieldDateToStupidDate(e.target.value)
            })}
            readonly={!isInEditMode}
            disabled={!request}
          />
          <DatePicker
            label={"Do"}
            value={request && stupidDateToDateFieldString(request?.endDate)}
            onChange={e => setRequest({
              ...request!,
              endDate: fieldDateToStupidDate(e.target.value)
            })}
            readonly={!isInEditMode}
            disabled={!request}
          />
        </HorizontalLayout>
      </Card>
      {
        request ?
          <>
            <EmployeeRequestConfigDialog
              key={employeeConfigDialog.selectedEmployee?.workerId}
              employee={employees.find(w => w.workerId === employeeConfigDialog.selectedEmployee?.workerId)!}
              isOpen={employeeConfigDialog.isOpen}
              onOpenChanged={(newValue) => setEmployeeConfigDialog(prevState => ({ ...prevState, isOpen: newValue }))}
              shiftsPerScheduleRequests={shiftPerScheduleRequests.filter(r => r.owner.workerId === employeeConfigDialog.selectedEmployee?.workerId)}
              onSave={handleEmployeeConfigSave}
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
                readonly={!isInEditMode}
              />
            </Card>

          </>
          : <h2>Vyber konfiguraci</h2>
      }
      <HorizontalLayout theme={"spacing"}>
      </HorizontalLayout>
    </VerticalLayout>
  )
}

