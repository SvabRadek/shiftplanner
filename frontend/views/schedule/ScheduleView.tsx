import { DatePicker } from "@hilla/react-components/DatePicker";
import { useForm } from "@hilla/react-form";
import { useContext, useEffect, useRef } from "react";
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
import { RequestCtx } from "Frontend/views/schedule/components/schedulegrid/RequestCtxProvider";
import PlannerConfigurationResponseModel
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationService/PlannerConfigurationResponseModel";

export default function ScheduleView() {

  const saveAsNewRef = useRef<boolean>(false);
  const requestCtx = useContext(RequestCtx)

  useEffect(() => {
    EmployeeService.getAllEmployees().then(requestCtx.setEmployees)
    window.addEventListener("beforeunload", handleUnload)
    return () => {
      window.removeEventListener("beforeunload", handleUnload)
    }
  }, []);

  useEffect(() => {
    form.read(requestCtx.request)
  }, [requestCtx.request]);

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
      requestCtx.setRequest(configResponse)
      ConstraintRequestService.getSpecificShiftRequests(configResponse.id).then(response => {
        response.forEach(req => {
          requestCtx.addSpecificShiftRequest(req.requestedShift, req.owner, req.date)
        })
      })
    })
  }

  return (
    <VerticalLayout theme={"spacing padding"}>
      <ConfigSelectDialog onConfigSelected={handleConfigSelected}/>
      <VerticalLayout theme={"spacing"}>
        <TextField
          label={"Nazev"}
          value={requestCtx.request?.name}
          onChange={e => requestCtx.setRequest({ ...requestCtx.request!, name: e.target.value })}
        />
        <HorizontalLayout theme={"spacing"}>
          <DatePicker
            label={"Od"}
            value={requestCtx.request?.startDate}
            onChange={e => requestCtx.setRequest({ ...requestCtx.request!, startDate: e.target.value })}
          />
          <DatePicker
            label={"Do"}
            value={requestCtx.request?.endDate}
            onChange={e => requestCtx.setRequest({ ...requestCtx.request!, endDate: e.target.value })}
          />
        </HorizontalLayout>
      </VerticalLayout>
      <HorizontalDivider/>
      {requestCtx.request ? <ScheduleGrid/> : <div>Select configuration first</div>}
      <HorizontalLayout theme={"spacing"}>
        <Button
          theme={"primary"}
          onClick={() => {
            saveAsNewRef.current = false
            form.submit().then()
          }}
          disabled={form.invalid || form.submitting}>
          Save
        </Button>
        <Button
          theme={"primary"}
          onClick={() => {
            saveAsNewRef.current = true
            form.submit().then()
          }}
          disabled={form.invalid || form.submitting}>
          Save As New
        </Button>
      </HorizontalLayout>
    </VerticalLayout>
  )
}
