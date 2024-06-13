import { Card } from "Frontend/components/Card";
import WorkerValidationIssue
  from "Frontend/generated/com/cocroachden/planner/constraint/validations/worker/WorkerValidationIssue";

type Props = {
  issue: WorkerValidationIssue
}

export function ValidationWorkerIssueCard(props: Props) {
  return (
    <Card>
      <span>{props.issue.severity}</span>
      <span>{props.issue.workerId.id}</span>
      <small>{props.issue.issue}</small>
    </Card>
  );
}
