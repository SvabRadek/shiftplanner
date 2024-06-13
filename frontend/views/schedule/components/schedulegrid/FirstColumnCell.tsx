import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { Tooltip } from "@hilla/react-components/Tooltip";
import { CSSProperties, ReactNode } from "react";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/repository/EmployeeRecord";
import EmployeeId from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeId";
import EmployeeValidationIssue
  from "Frontend/generated/com/cocroachden/planner/constraint/validations/employee/EmployeeValidationIssue";

const defaultStyle: CSSProperties = {
  cursor: "pointer",
  display: "flex",
  userSelect: "none",
  width: 250,
  height: 50,
  border: "solid",
  borderColor: "var(--lumo-tint-20pct)",
  borderWidth: "1px",
  justifyContent: "space-between",
  alignItems: "center",
  backgroundColor: "var(--lumo-shade-5pct)",
  paddingLeft: 10,
  paddingRight: 10,
}

type Props = {
  owner: EmployeeId,
  title: ReactNode
  onEmployeeAction: (action: CrudAction<Pick<EmployeeRecord, "id">>) => void
  issues: EmployeeValidationIssue[]
  readonly?: boolean
  style?: CSSProperties
}

export function FirstColumnCell(props: Props) {

  const cellStyle = Object.assign({}, defaultStyle, props.style)

  function handleLeftClick() {
    props.onEmployeeAction({
      type: CRUDActions.READ,
      payload: props.owner
    })
  }

  return (
    <div
      id={"headerCell" + props.owner}
      onClick={handleLeftClick}
      style={cellStyle}
    >
      {props.title}
      {props.issues.length > 0
        && <Tooltip
              for={"headerCell" + props.owner}
              text={props.issues.map(i => i.issue).join("\n")}
              position={"top-end"}
          />}
    </div>
  )
}
