import { DatePicker } from "@hilla/react-components/DatePicker";
import { useForm } from "@hilla/react-form";
import { useEffect, useRef, useState } from "react";
import { ConstraintEndpoint, EmployeeService, PlannerConfigurationService } from "Frontend/generated/endpoints";
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
import { EmployeeRequestConfig } from "Frontend/views/schedule/components/EmployeeRequestConfig";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import PlannerConfigurationDTOModel
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTOModel";
import PlannerConfigurationMetaDataDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationMetaDataDTO";
import SpecificShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/SpecificShiftRequestDTO";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import WorkerId from "Frontend/generated/com/cocroachden/planner/lib/WorkerId";

type EmployeeConfigDialogParams = {
  isOpen: boolean,
  selectedEmployee?: WorkerId
}

export default function ScheduleView() {

  const saveAsNewRef = useRef<boolean>(false);
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

  useEffect(() => {
    form.read(request)
  }, [request]);

  function handleUnload(e: Event) {
    e.preventDefault()
  }

  const form = useForm(PlannerConfigurationDTOModel, {
    onSubmit: async value => {
      if (saveAsNewRef.current) {
        await PlannerConfigurationService.saveAsNew(value).then(form.read)
      } else {
        await PlannerConfigurationService.upsert(value).then(form.read)
      }
    }
  })

  function handleConfigSelected(value: PlannerConfigurationMetaDataDTO) {
    PlannerConfigurationService.getConfiguration(value.id).then(configResponse => {
      setRequest(configResponse)
      ConstraintEndpoint.findSpecificShiftRequests(configResponse.id).then(setShiftRequests)
      ConstraintEndpoint.findShiftsPerScheduleRequests(configResponse.id).then(setShiftPerScheduleRequests)
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

  return (
    <VerticalLayout theme={"spacing padding"}>
      <ConfigSelectDialog onConfigSelected={handleConfigSelected}/>
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
              employee={employees.find(w => w.workerId === employeeConfigDialog.selectedEmployee?.workerId)!}
              isOpen={employeeConfigDialog.isOpen}
              onOpenChanged={(newValue) => setEmployeeConfigDialog(prevState => ({ ...prevState, isOpen: newValue }))}
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
              onEmployeeAction={handleEmployeeAction}
            />
          </>
          : <h2>Vyber konfiguraci</h2>
      }
      <HorizontalLayout theme={"spacing"}>
        <Button
          theme={"primary"}
          onClick={() => {
            saveAsNewRef.current = false
            form.submit().then()
          }}
          disabled={form.invalid || form.submitting || !request}>
          Save
        </Button>
        <Button
          theme={"primary"}
          onClick={() => {
            saveAsNewRef.current = true
            form.submit().then()
          }}
          disabled={form.invalid || form.submitting || !request}>
          Save As New
        </Button>
      </HorizontalLayout>
    </VerticalLayout>
  )
}
