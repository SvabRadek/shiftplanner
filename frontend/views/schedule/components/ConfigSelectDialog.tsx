import { Dialog } from "@hilla/react-components/Dialog";
import { useEffect, useState } from "react";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { PlannerConfigList } from "Frontend/views/schedule/components/PlannerConfigList";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import PlannerConfigurationMetaDataDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationMetaDataDTO";
import { PlannerConfigurationEndpoint } from "Frontend/generated/endpoints";

type Props = {
  onConfigSelected?: (value: PlannerConfigurationMetaDataDTO) => void
  isOpen?: boolean
}

export function ConfigSelectDialog(props: Props) {

  const [isOpen, setIsOpen] = useState(props.isOpen);
  const [selectedItem, setSelectedItem] = useState<PlannerConfigurationMetaDataDTO>()
  const [configMetaData, setConfigMetaData] = useState<PlannerConfigurationMetaDataDTO[]>([])

  useEffect(() => {
    setIsOpen(props.isOpen)
  }, [props.isOpen]);

  function handleOpen() {
    setSelectedItem(undefined)
    PlannerConfigurationEndpoint.getMetaData().then(setConfigMetaData)
    setIsOpen(true)
  }

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
        <VerticalLayout theme={"spacing"} style={{ minWidth: "700px", minHeight: "400px" }}>
          <PlannerConfigList configList={configMetaData} onSelectionChanged={setSelectedItem}/>
          <HorizontalLayout style={{ width: "100%", justifyContent: "end" }} theme={"spacing"}>
            <Button theme={"primary"} disabled={!selectedItem} onClick={confirmSelection}>Vybrat</Button>
            <Button onClick={() => setIsOpen(false)}>Zrusit</Button>
          </HorizontalLayout>
        </VerticalLayout>
      </Dialog>
      <Button theme={"icon primary"} onClick={handleOpen}>
        <Icon icon={"vaadin:cog"} slot={"prefix"}/>
          Vybrat konfiguraci
      </Button>
    </>
  );
}
