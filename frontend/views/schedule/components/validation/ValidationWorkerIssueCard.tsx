import { Card } from "Frontend/components/Card";
import EmployeeValidationIssue
  from "Frontend/generated/com/cocroachden/planner/constraint/validations/employee/EmployeeValidationIssue";

type Props = {
  issue: EmployeeValidationIssue
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
