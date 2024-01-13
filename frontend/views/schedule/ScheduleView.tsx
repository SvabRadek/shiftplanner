import { DatePicker } from "@hilla/react-components/DatePicker";
import { useForm } from "@hilla/react-form";
import { useEffect, useRef } from "react";
import PlannerConfigurationRecordModel
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationRecordModel";
import { PlannerConfigurationService } from "Frontend/generated/endpoints";
import { Button } from "@hilla/react-components/Button";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { TextField } from "@hilla/react-components/TextField";
import { ScheduleGrid } from "Frontend/views/schedule/ScheduleGrid";
import { HorizontalDivider } from "Frontend/components/HorizontalDivider";
import PlannerConfigurationRecord
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationRecord";
import { useLocalStorage } from "@uidotdev/usehooks";
import { PlannerConfigList } from "Frontend/views/schedule/PlannerConfigList";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import PlannerConfigurationMetaData
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationMetaData";

export default function ScheduleView() {

  const saveAsNewRef = useRef<boolean>(false);
  const [request, setRequest] = useLocalStorage<PlannerConfigurationRecord | undefined>(
    "planner_config", undefined
  )

  useEffect(() => {
    if (!request) {
      PlannerConfigurationService.getLatestConfiguration().then(setRequest);
    }
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

  const form = useForm(PlannerConfigurationRecordModel, {
    onSubmit: async value => {
      if (saveAsNewRef.current) {
        await PlannerConfigurationService.saveAsNew(value).then(form.read)
      } else {
        await PlannerConfigurationService.upsert(value).then(form.read)
      }
    }
  })

  function handleConfigSelectionChanged(value: PlannerConfigurationMetaData) {
    PlannerConfigurationService.getConfiguration(value.id).then(setRequest)
  }

  return (
    <VerticalLayout theme={"spacing padding"}>
      <PlannerConfigList onSelectionChanged={handleConfigSelectionChanged} />
      <VerticalLayout theme={"spacing"}>
        <TextField
          label={"Nazev"}
          value={request?.name}
          onChange={e => setRequest(old => ({ ...old!, name: e.target.value }))}
        />
        <HorizontalLayout theme={"spacing"}>
          <DatePicker
            label={"Od"}
            value={request?.startDate}
            onChange={e => setRequest(old => ({ ...old!, startDate: e.target.value }))}
          />
          <DatePicker
            label={"Do"}
            value={request?.endDate}
            onChange={e => setRequest(old => ({ ...old!, endDate: e.target.value }))}
          />
        </HorizontalLayout>
      </VerticalLayout>
      <HorizontalDivider/>
      {request && <ScheduleGrid config={request}/>}
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
