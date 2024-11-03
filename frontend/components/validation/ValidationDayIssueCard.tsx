import { Card } from "Frontend/components/Card";
import DayValidationIssueDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/validations/day/DayValidationIssueDTO";

type Props = {
  issue: DayValidationIssueDTO
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
