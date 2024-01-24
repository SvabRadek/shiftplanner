import { DatePicker } from "@hilla/react-components/DatePicker";
import { useForm } from "@hilla/react-form";
import { useEffect, useRef, useState } from "react";
import { ConstraintRequestService, EmployeeService, PlannerConfigurationService } from "Frontend/generated/endpoints";
import { Button } from "@hilla/react-components/Button";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { TextField } from "@hilla/react-components/TextField";
import { HorizontalDivider } from "Frontend/components/HorizontalDivider";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import PlannerConfigurationMetaData
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationMetaData";
import { ConfigSelectDialog } from "Frontend/views/schedule/components/ConfigSelectDialog";
import PlannerConfigurationResponseModel
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationService/PlannerConfigurationResponseModel";
import PlannerConfigurationResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationService/PlannerConfigurationResponse";
import SpecificShiftRequestResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/ConstraintRequestService/SpecificShiftRequestResponse";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { ScheduleGridContainer } from "./components/schedulegrid/ScheduleGridContainer";
import { EmployeeSelectDialog } from "Frontend/views/schedule/components/EmployeeSelectDialog";
import { EmployeeAction, EmployeeActionEnum } from "Frontend/views/schedule/components/schedulegrid/GridNameCell";

export default function ScheduleView() {

  const saveAsNewRef = useRef<boolean>(false);
  const [request, setRequest] = useState<PlannerConfigurationResponse | undefined>()
  const [employees, setEmployees] = useState<EmployeeRecord[]>([])
  const [shiftRequests, setShiftRequests] = useState<SpecificShiftRequestResponse[]>([])
  const [isAddEmployeeDialogOpen, setIsAddEmployeeDialogOpen] = useState(false)

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

  const form = useForm(PlannerConfigurationResponseModel, {
    onSubmit: async value => {
      if (saveAsNewRef.current) {
        await PlannerConfigurationService.saveAsNew(value).then(form.read)
      } else {
        await PlannerConfigurationService.upsert(value).then(form.read)
      }
    }
  })

  function handleConfigSelected(value: PlannerConfigurationMetaData) {
    PlannerConfigurationService.getConfiguration(value.id).then(configResponse => {
      setRequest(configResponse)
      ConstraintRequestService.getSpecificShiftRequests(configResponse.id).then(setShiftRequests)
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
        request
          ?
          <>
            <EmployeeSelectDialog
              employees={employees}
              selectedWorkers={request.workers}
              onEmployeeSelected={handleAddEmployee}
              onOpenChanged={value => setIsAddEmployeeDialogOpen(value)}
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
