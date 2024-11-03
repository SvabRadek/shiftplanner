import {Dialog} from "@hilla/react-components/Dialog";
import {useEffect, useState} from "react";
import {Button} from "@hilla/react-components/Button";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import "@vaadin/icons";
import SolverConfigurationMetadata
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationMetadata";
import {SolverConfigurationEndpoint} from "Frontend/generated/endpoints";
import {GridColumn} from "@hilla/react-components/GridColumn";
import {Icon} from "@hilla/react-components/Icon";
import {Grid} from "@hilla/react-components/Grid";

type Props = {
    onConfigSelected: (selected: SolverConfigurationMetadata) => void
    onOpenChanged: (value: boolean) => void
    isOpen: boolean
}

export function ConfigSelectDialog(props: Props) {

    const [configurationMetadata, setConfigurationMetadata] = useState<SolverConfigurationMetadata[]>([]);
    const [selectedItems, setSelectedItems] = useState<SolverConfigurationMetadata[]>([])

    useEffect(() => {
        if (props.isOpen) {
            SolverConfigurationEndpoint.getMetaData().then(setConfigurationMetadata);
        }
    }, [props.isOpen]);

    async function handleConfigDelete(metadata: SolverConfigurationMetadata) {
        await SolverConfigurationEndpoint.delete(metadata.id)
        SolverConfigurationEndpoint.getMetaData().then(setConfigurationMetadata);
    }

    function handleConfigSelect() {
        if (selectedItems.length > 0) {
            props.onConfigSelected(selectedItems[0])
        }
        props.onOpenChanged(false)
    }

    return (
        <Dialog
            header-title={"Vyber konfiguraci"}
            opened={props.isOpen}
            onOpenedChanged={e => {
                props.onOpenChanged(e.detail.value)
            }}
        >
            <VerticalLayout theme={"spacing"} style={{width: "75vw", minHeight: "400px"}}>
                <Grid
                    items={configurationMetadata}
                    style={{ height: 200 }}
                    selectedItems={selectedItems}
                    onActiveItemChanged={(e) => {
                        const item = e.detail.value
                        setSelectedItems(item ? [item] : [])
                    }}
                >
                    <GridColumn header={"Jméno"} path={"name"}/>
                    <GridColumn header={"Od"} path={"startDate"}/>
                    <GridColumn header={"Do"} path={"endDate"}/>
                    <GridColumn header={"Vytvořeno"} path={"createdAt"}/>
                    <GridColumn header={"Id"} path={"id"}/>
                    <GridColumn header={"Akce"} flexGrow={0}>
                        {(row) =>
                            <Button theme={"small icon"} onClick={() => handleConfigDelete(row.item)}>
                                <Icon icon={"vaadin:trash"}></Icon>
                            </Button>}
                    </GridColumn>
                </Grid>
                <HorizontalLayout style={{width: "100%", justifyContent: "end"}} theme={"spacing"}>
                    <Button theme={"primary"} disabled={selectedItems.length === 0} onClick={handleConfigSelect}>Vybrat</Button>
                    <Button onClick={() => props.onOpenChanged(false)}>Zrušit</Button>
                </HorizontalLayout>
            </VerticalLayout>
        </Dialog>
    );
}
