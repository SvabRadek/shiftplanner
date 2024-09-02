import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { Tooltip } from "@hilla/react-components/Tooltip";
import { CSSProperties } from "react";
import DayValidationIssue
  from "Frontend/generated/com/cocroachden/planner/constraint/validations/day/DayValidationIssue";

const defaultStyle: CSSProperties = {
  display: "flex",
  userSelect: "none",
  width: 50,
  height: 100,
  border: "solid",
  borderColor: "var(--lumo-tint-20pct)",
  borderWidth: "1px",
  justifyContent: "center",
  alignItems: "center",
  backgroundColor: "var(--lumo-shade-5pct)"
}

type Props = {
  title: string
  secondaryTitle?: string,
  hint?: string,
  style?: CSSProperties
  issues: DayValidationIssue[]
}

export function FirstRowCell(props: Props) {
  const cellId = "headercell" + props.title
  let clippedSecTitle = props.secondaryTitle
  if (clippedSecTitle && clippedSecTitle.length > 9) {
    clippedSecTitle = clippedSecTitle.substring(0, 8) + "..."
  }

  const cellStyle = Object.assign({}, defaultStyle, props.style)

  return (
    <div
      id={cellId}
      style={cellStyle}>
      <VerticalLayout style={{justifyContent: "center", alignItems: "center"}}>
        {props.title}
        {clippedSecTitle ? <em style={{ fontSize: 10 }}>{clippedSecTitle}</em> : null}
      </VerticalLayout>
      {props.hint ? <Tooltip for={cellId} text={props.hint} position={"top"}/> : null}
    </div>
  );
}
