import { ContextMenu, ContextMenuItem } from "@hilla/react-components/ContextMenu";
import { useContext } from "react";
import { RequestCtx } from "Frontend/views/schedule/components/schedulegrid/RequestCtxProvider";

type Props = {
  title: string
  workerId: string
  onAddEmployee: () => void
}

const menuItems: ContextMenuItem[] = [
  {
    text: "Upravit"
  },
  {
    text: "Odstranit"
  },
  {
    text: "Pridat"
  }
]

export function NameCell(props: Props) {

  const requestCtx = useContext(RequestCtx)

  function handleMenuSelection(item: ContextMenuItem) {
    if (item.text === "Odstranit") {
      requestCtx.removeEmployeeFromRequest(props.workerId)
    }
    if (item.text === "Pridat") {
      props.onAddEmployee()
    }
  }

  return (
    <ContextMenu
      items={menuItems}
      onItemSelected={e => handleMenuSelection(e.detail.value)}
    >
      <div style={{ display: "flex" }}>
        {props.title}
      </div>
    </ContextMenu>
  );
}
