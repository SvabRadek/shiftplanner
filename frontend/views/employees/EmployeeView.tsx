import { EmployeeService } from "Frontend/generated/endpoints";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { AutoCrud } from "@hilla/react-crud";
import EmployeeRecordModel from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecordModel";

export default function EmployeeView() {
  return (
    <div className="p-m">
      <VerticalLayout theme={"spacing padding"}>
        <AutoCrud
          style={{ width: "100%" }}
          service={EmployeeService}
          model={EmployeeRecordModel}
        />
      </VerticalLayout>
    </div>
  );
}