import { FormLayout } from "@hilla/react-components/FormLayout";
import { DatePicker } from "@hilla/react-components/DatePicker";
import { FormLayoutResponsiveStep } from "@vaadin/form-layout/src/vaadin-form-layout";
import { useForm } from "@hilla/react-form";
import { useEffect, useState } from "react";
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

const breakPoints: FormLayoutResponsiveStep[] = [
  { minWidth: "0", columns: 1 },
  { minWidth: "500px", columns: 2 }
]

type SubmitParams = {
  submitting: boolean,
  asUpdate: boolean
}

export default function ScheduleView() {

  const [submitFlag, setSubmitFlag] = useState<SubmitParams>({
    submitting: false,
    asUpdate: false
  })
  const [request, setRequest] = useLocalStorage<PlannerConfigurationRecord | undefined>(
    "planner_config", undefined
  )
  useEffect(() => {
    if (!request) {
      PlannerConfigurationService.getLatestConfiguration().then(setRequest);
    }
  }, []);

  useEffect(() => {
    form.read(request)
  }, [request]);

  const form = useForm(PlannerConfigurationRecordModel, {
    onSubmit: async value => {
      if (submitFlag.asUpdate) {
        await PlannerConfigurationService.upsert(value).then(form.read)
      } else {
        await PlannerConfigurationService.saveAsNew(value).then(form.read)
      }
    }
  })

  async function handleSubmit(asUpdate: boolean) {
    setSubmitFlag({ submitting: true, asUpdate })
  }

  useEffect(() => {
    if (submitFlag.submitting) {
      form.submit().then(() => setSubmitFlag({ submitting: false, asUpdate: submitFlag.asUpdate }))
    }
  }, [submitFlag]);

  return (
    <div className="p-m">
      <PlannerConfigList/>
      <FormLayout responsiveSteps={breakPoints}>
        <TextField
          label={"Nazev"}
          {...{ colspan: 2 }}
          value={request?.name}
          onChange={e => setRequest(old => ({ ...old!, name: e.target.value }))}
        />
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
      </FormLayout>
      <HorizontalDivider/>
      {request && <ScheduleGrid config={request}/>}
      <HorizontalLayout theme={"spacing"}>
        <Button
          theme={"primary"}
          onClick={() => handleSubmit(true)}
          disabled={form.invalid || submitFlag.submitting}>
          Save
        </Button>
        <Button
          theme={"primary"}
          onClick={() => handleSubmit(false)}
          disabled={form.invalid || submitFlag.submitting}>
          Save As New
        </Button>
      </HorizontalLayout>
    </div>
  )
}
