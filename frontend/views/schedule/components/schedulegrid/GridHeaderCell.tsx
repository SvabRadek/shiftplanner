import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { Tooltip } from "@hilla/react-components/Tooltip";
import DayValidationIssue from "Frontend/generated/com/cocroachden/planner/constraint/validations/DayValidationIssue";

type Props = {
  title: string
  secondaryTitle?: string,
  hint?: string,
  backgroundColor?: string
  issues: DayValidationIssue[]
}

export function GridHeaderCell(props: Props) {
  const cellId = "headercell" + props.title
  let clippedSecTitle = props.secondaryTitle
  if (clippedSecTitle && clippedSecTitle.length > 9) {
    clippedSecTitle = clippedSecTitle.substring(0, 8) + "..."
  }
  return (
    <div
      id={cellId}
      style={{
        display: "flex",
        userSelect: "none",
        width: 50,
        height: 50,
        border: "solid",
        borderColor: "var(--lumo-tint-20pct)",
        borderWidth: "1px",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: props.backgroundColor || "var(--lumo-shade-5pct)"
      }}>
      <VerticalLayout
        style={{
          justifyContent: "center",
          alignItems: "center"
        }}
      >
        {props.title}
        {clippedSecTitle ? <em style={{ fontSize: 10 }}>{clippedSecTitle}</em> : null}
      </VerticalLayout>
      {props.hint ? <Tooltip for={cellId} text={props.hint} position={"top"}/> : null}
    </div>
  );
}
