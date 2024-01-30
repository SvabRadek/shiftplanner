import { ContextMenu, ContextMenuItem, ContextMenuItemSelectedEvent } from "@hilla/react-components/ContextMenu";

type Props = {
  workerId: string,
  title: string
  backgroundColor?: string
  onEmployeeAction?: (action: EmployeeAction) => void
  readonly?: boolean
}

export type EmployeeAction = {
  type: string,
  workerId: string
}

export enum EmployeeActionEnum {
  EDIT = "Detail",
  DELETE = "Odstranit",
  ADD = "Pridat"
}

export function GridNameCell(props: Props) {

  const cellContextMenuItems: ContextMenuItem[] = Object.values(EmployeeActionEnum)
    .map(value => ({ text: value, disabled: !!(props.readonly && value !== "Detail") }))

  function handleContextMenuSelection(e: ContextMenuItemSelectedEvent) {
    const item = e.detail.value
    props.onEmployeeAction?.({
      type: item.text!,
      workerId: props.workerId
    })
  }

  return (
      <ContextMenu items={cellContextMenuItems} onItemSelected={handleContextMenuSelection}>
        <div style={{
          display: "flex",
          userSelect: "none",
          width: 200,
          height: 50,
          border: "solid",
          borderColor: "var(--lumo-tint-20pct)",
          borderWidth: "1px",
          justifyContent: "start",
          alignItems: "center",
          backgroundColor: props.backgroundColor || "var(--lumo-shade-5pct)",
          paddingLeft: 10
        }}>
          {props.title}
        </div>
      </ContextMenu>
  )
}
