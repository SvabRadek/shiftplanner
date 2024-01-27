import { Dialog } from "@hilla/react-components/Dialog";
import { useEffect, useState } from "react";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { PlannerConfigList } from "Frontend/views/schedule/components/PlannerConfigList";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";

type Props = {
  onConfigSelected?: (value: PlannerConfigurationDTO) => void
  isOpen?: boolean
}

export function ConfigSelectDialog(props: Props) {

  const [isOpen, setIsOpen] = useState(props.isOpen);
  const [selectedItem, setSelectedItem] = useState<PlannerConfigurationDTO>()

  useEffect(() => {
    setIsOpen(props.isOpen)
  }, [props.isOpen]);
  
  function confirmSelection() {
    if (props.onConfigSelected && selectedItem) {
      props.onConfigSelected(selectedItem)
    }
    setIsOpen(false)
  }

  return (
    <>
      <Dialog
        header-title={"Vyber konfiguraci"}
        opened={isOpen}
        onOpenedChanged={e => {
          setIsOpen(e.detail.value)
        }}
      >
        <VerticalLayout style={{ minWidth: "700px", minHeight: "400px" }}>
          <PlannerConfigList onSelectionChanged={setSelectedItem}/>
          <HorizontalLayout style={{ width: "100%", justifyContent: "end" }} theme={"padding spacing"}>
            <Button onClick={() => setIsOpen(false)}>Zrusit</Button>
            <Button disabled={!selectedItem} onClick={confirmSelection}>Vybrat</Button>
          </HorizontalLayout>
        </VerticalLayout>
      </Dialog>
      <Button theme={"icon primary"} onClick={() => setIsOpen(true)}>
        <Icon icon={"vaadin:cog"} slot={"prefix"}/>
          Vybrat konfiguraci
      </Button>
    </>
  );
}
