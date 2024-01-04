import { FormLayout } from "@hilla/react-components/FormLayout";
import { DatePicker } from "@hilla/react-components/DatePicker";
import { FormLayoutResponsiveStep } from "@vaadin/form-layout/src/vaadin-form-layout";
import { useForm } from "@hilla/react-form";
import SchedulePlanConfigurationModel
  from "Frontend/generated/com/cocroachden/planner/solver/SchedulePlanConfigurationModel";

const breakPoints: FormLayoutResponsiveStep[] = [
  { minWidth: "0", columns: 1 },
  { minWidth: "500px", columns: 2 }
]

export default function ScheduleView() {
  const { model, field } = useForm(SchedulePlanConfigurationModel)

  return (
    <div className="p-m">
      <FormLayout responsiveSteps={breakPoints}>
        <DatePicker
          label={"Od"}
          {...field(model.startDate)}
        />
        <DatePicker
          label={"Do"}
          {...field(model.endDate)}
        />
      </FormLayout>
      <div style={{ width: 100, height: 100 }}
           className={"card"}>

      </div>
    </div>
  );
}
