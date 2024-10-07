import {Dialog} from "@hilla/react-components/Dialog";
import {useState} from "react";
import {Button} from "@hilla/react-components/Button";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {PlannerConfigList} from "Frontend/views/schedule/components/PlannerConfigList";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {CrudAction, CRUDActions} from "Frontend/util/utils";
import "@vaadin/icons";
import SolverConfigurationMetadata
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationMetadata";
import {ConfigUploadDialog} from "Frontend/views/schedule/components/ConfigUploadDialog";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import {SolverConfigurationEndpoint} from "Frontend/generated/endpoints";

type Props = {
    configMetaData: SolverConfigurationMetadata[]
    onConfigAction: (action: CrudAction<SolverConfigurationMetadata>) => void
    onOpenChanged: (value: boolean) => void
    isOpen: boolean
}

export function ConfigSelectDialog(props: Props) {

    const [selectedItem, setSelectedItem] = useState<SolverConfigurationMetadata>()
    const [isImportOpen, setIsImportOpen] = useState(false);

    function handleConfigSelect() {
        if (selectedItem) {
            props.onConfigAction({
                type: CRUDActions.READ,
                payload: selectedItem
            })
        }
        props.onOpenChanged(false)
    }

    async function handleConfigUpload(config: SolverConfigurationDTO) {
        await SolverConfigurationEndpoint.save(config)
        props.onConfigAction({
            type: CRUDActions.UPDATE,
            payload: config
        })
    }

    return (
        <Dialog
            header-title={"Vyber konfiguraci"}
            opened={props.isOpen}
            onOpenedChanged={e => {
                props.onOpenChanged(e.detail.value)
            }}
        >
            <VerticalLayout theme={"spacing"} style={{ width: "75vw", minHeight: "400px"}}>
                <PlannerConfigList onAction={props.onConfigAction}
                                   configList={props.configMetaData}
                                   onSelectionChanged={setSelectedItem}
                />
                <HorizontalLayout style={{width: "100%", justifyContent: "end"}} theme={"spacing"}>
                    <Button theme={"primary"} disabled={!selectedItem} onClick={handleConfigSelect}>Vybrat</Button>
                    <Button onClick={() => setIsImportOpen(true)}>Importovat</Button>
                    <Button onClick={() => props.onOpenChanged(false)}>Zrusit</Button>
                </HorizontalLayout>
            </VerticalLayout>
            <ConfigUploadDialog
                isOpen={isImportOpen}
                onOpenChanged={setIsImportOpen}
                onImport={handleConfigUpload}
            />
        </Dialog>
    );
}
