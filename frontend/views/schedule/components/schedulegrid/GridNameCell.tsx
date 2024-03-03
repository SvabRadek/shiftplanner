import { ContextMenu, ContextMenuItem, ContextMenuItemSelectedEvent } from "@hilla/react-components/ContextMenu";
import { CrudAction, CRUDActions } from "Frontend/util/utils";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import ValidatorIssue from "Frontend/generated/com/cocroachden/planner/solver/constraints/validator/ValidatorIssue";
import { Tooltip } from "@hilla/react-components/Tooltip";

type Props = {
  workerId: string,
  title: string
  backgroundColor?: string
  onEmployeeAction: (action: CrudAction<Pick<EmployeeRecord, "workerId">>) => void
  issues: ValidatorIssue[]
  readonly?: boolean
  disableContextMenu?: boolean
}

export function GridNameCell(props: Props) {

  const contextMenuItems: ContextMenuItem[] = [
    {
      text: "Detail",
      disabled: false
    },
    {
      text: "Odebrat",
      disabled: props.readonly
    }
  ]

  function handleContextMenuSelection(e: ContextMenuItemSelectedEvent) {
    const item = e.detail.value
    switch (item.text) {
      case "Odebrat":
        props.onEmployeeAction({
          type: CRUDActions.DELETE,
          payload: { workerId: props.workerId }
        })
        break
      case "Detail":
        props.onEmployeeAction({
          type: CRUDActions.READ,
          payload: { workerId: props.workerId }
        })
    }
  }

  const titleParts = props.title.split(";")

  function renderCenter() {
    return (
      <div
        id={"headerCell" + props.workerId}
        style={{
        display: "flex",
        userSelect: "none",
        width: 200,
        height: 50,
        border: "solid",
        borderColor: "var(--lumo-tint-20pct)",
        borderWidth: "1px",
        justifyContent: "space-between",
        alignItems: "center",
        backgroundColor: props.backgroundColor || "var(--lumo-shade-5pct)",
        paddingLeft: 10,
        paddingRight: 10,
      }}>
        {titleParts.map(p =>
          <span key={p}>
            {p}
          </span>
        )}
        {props.issues.length > 0 && <Tooltip for={"headerCell" + props.workerId} text={props.issues.map(i => i.issue).join("\n")} position={"top-end"}/>}
      </div>
    )
  }

  return (
    props.disableContextMenu ?
      renderCenter()
      :
      <ContextMenu items={contextMenuItems} onItemSelected={handleContextMenuSelection}>
        {renderCenter()}
      </ContextMenu>
  )
}
