import { DatePicker } from "@hilla/react-components/DatePicker";
import { useEffect, useState } from "react";
import { ConstraintEndpoint, EmployeeService, PlannerConfigurationEndpoint } from "Frontend/generated/endpoints";
import { Button } from "@hilla/react-components/Button";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { TextField } from "@hilla/react-components/TextField";
import { HorizontalDivider } from "Frontend/components/HorizontalDivider";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { ConfigSelectDialog } from "Frontend/views/schedule/components/ConfigSelectDialog";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { ScheduleGridContainer } from "./components/schedulegrid/ScheduleGridContainer";
import { AddEmployeeDialog } from "Frontend/views/schedule/components/AddEmployeeDialog";
import { EmployeeAction, EmployeeActionEnum } from "Frontend/views/schedule/components/schedulegrid/GridNameCell";
import {
  EmployeeConfigModel,
  EmployeeRequestConfig
} from "Frontend/views/schedule/components/employeeConstraints/EmployeeRequestConfig";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import SpecificShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/SpecificShiftRequestDTO";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import WorkerId from "Frontend/generated/com/cocroachden/planner/lib/WorkerId";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/lib/ConstraintType";
import { areShiftRequestsSame } from "Frontend/util/utils";

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
      workers: employees.map(e => ({ workerId: e.workerId })),
      constraintRequestInstances: [
        ...specificShiftIds.map(id => ({ requestType: ConstraintType.SPECIFIC_SHIFT_REQUEST, requestId: id })),
        ...shiftPerScheduleIds.map(id => ({ requestType: ConstraintType.SHIFT_PER_SCHEDULE, requestId: id }))
      ]
    }).then(response => handleConfigSelected(response))
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
        ...changedRequests
      ]
    })
  }

  return (
    <VerticalLayout theme={"spacing padding"}>
      <ConfigSelectDialog onConfigSelected={value => handleConfigSelected(value.id)}/>
      <VerticalLayout theme={"spacing"}>
        <TextField
          label={"Nazev"}
          value={request?.name}
          onChange={e => setRequest({ ...request!, name: e.target.value })}
          disabled={!request}
        />
        <HorizontalLayout theme={"spacing"}>
          <DatePicker
            label={"Od"}
            value={request?.startDate}
            onChange={e => setRequest({ ...request!, startDate: e.target.value })}
            disabled={!request}
          />
          <DatePicker
            label={"Do"}
            value={request?.endDate}
            onChange={e => setRequest({ ...request!, endDate: e.target.value })}
            disabled={!request}
          />
        </HorizontalLayout>
      </VerticalLayout>
      <HorizontalDivider/>
      {
        request ?
          <>
            <EmployeeRequestConfig
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
            <ScheduleGridContainer
              request={request}
              employees={employees}
              shiftRequests={shiftRequests}
              shiftPerScheduleRequests={shiftPerScheduleRequests}
              onEmployeeAction={handleEmployeeAction}
              onShiftRequestsChanged={handleShiftRequestsChanged}
            />
          </>
          : <h2>Vyber konfiguraci</h2>
      }
      <HorizontalLayout theme={"spacing"}>
        <Button
          theme={"primary"}
          onClick={handleSave}
          disabled={!request}>
          Save
        </Button>
      </HorizontalLayout>
    </VerticalLayout>
  )
}

