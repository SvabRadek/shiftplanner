import { Card } from "Frontend/components/Card";
import EmployeeValidationIssueDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/validations/employee/EmployeeValidationIssueDTO";

type Props = {
  issue: EmployeeValidationIssueDTO
}

export function ValidationWorkerIssueCard(props: Props) {
  return (
    <Card>
      <span>{props.issue.severity}</span>
      <span>{props.issue.employeeId}</span>
      <small>{props.issue.issue}</small>
    </Card>
  );
}
