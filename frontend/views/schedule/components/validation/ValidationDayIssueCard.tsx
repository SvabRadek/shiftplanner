import { Card } from "Frontend/components/Card";
import DayValidationIssue from "Frontend/generated/com/cocroachden/planner/constraint/validations/day/DayValidationIssue";

type Props = {
  issue: DayValidationIssue
}

export function ValidationDayIssueCard(props: Props) {
  return (
    <Card>
      <span>{props.issue.severity}</span>
      <span>{props.issue.localDate}</span>
      <small>{props.issue.issue}</small>
    </Card>
  );
}
