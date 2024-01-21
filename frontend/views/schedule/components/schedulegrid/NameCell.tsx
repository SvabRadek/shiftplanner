import { ContextMenu, ContextMenuItem } from "@hilla/react-components/ContextMenu";
import { Operation } from "Frontend/util/types";
import { memo } from "react";

type Props = {
  title: string
  workerId: string
  onContextOperation?: (workerId: string, operation: Operation) => void
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

export const NameCell = memo(function NameCell(props: Props) {

  function handleMenuSelection(item: ContextMenuItem) {
    if (item.text === "Odstranit") {
      props.onContextOperation?.(props.workerId, Operation.DELETE)
    }
    if (item.text === "Pridat") {
      props.onContextOperation?.(props.workerId, Operation.ADD)
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
}, (prevProps, nextProps) => {
  return prevProps.title === nextProps.title && prevProps.workerId === nextProps.workerId
})
