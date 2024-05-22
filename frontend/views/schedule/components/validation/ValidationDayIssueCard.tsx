import { Card } from "Frontend/components/Card";
import DayValidationIssue from "Frontend/generated/com/cocroachden/planner/constraint/validations/DayValidationIssue";
import { stupidDateToString } from "Frontend/util/utils";

type Props = {
  issue: DayValidationIssue
}

export function ValidationDayIssueCard(props: Props) {
  return (
    <Card>
      <span>{props.issue.severity}</span>
      <span>{stupidDateToString(props.issue.localDate)}</span>
      <small>{props.issue.issue}</small>
    </Card>
  );
}
