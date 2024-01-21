import { DatePicker } from "@hilla/react-components/DatePicker";
import { useForm } from "@hilla/react-form";
import { useEffect, useRef, useState } from "react";
import { ConstraintRequestService, EmployeeService, PlannerConfigurationService } from "Frontend/generated/endpoints";
import { Button } from "@hilla/react-components/Button";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { TextField } from "@hilla/react-components/TextField";
import { ScheduleGrid } from "Frontend/views/schedule/components/schedulegrid/ScheduleGrid";
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

export default function ScheduleView() {

  const saveAsNewRef = useRef<boolean>(false);
  const [request, setRequest] = useState<PlannerConfigurationResponse | undefined>()
  const [employees, setEmployees] = useState<EmployeeRecord[]>([])
  const [shiftRequests, setShiftRequests] = useState<SpecificShiftRequestResponse[]>([])

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
          ? <ScheduleGrid
            request={request}
            employees={employees}
            shiftRequests={shiftRequests}
          />
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
