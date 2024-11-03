import {Card} from "Frontend/components/Card";
import {Button} from "@hilla/react-components/Button";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {CSSProperties, useState} from "react";
import {ConfigSelectDialog} from "Frontend/components/configurationselect/ConfigSelectDialog";
import {ConfigUploadDialog} from "Frontend/components/configurationselect/ConfigUploadDialog";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import SolverConfigurationMetadata
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationMetadata";
import {SolverConfigurationEndpoint} from "Frontend/generated/endpoints";
import {generateUUID} from "Frontend/util/utils";

const buttonStyle: CSSProperties = {
    width: "250px"
}
const buttonTheme = "primary"
type Props = {
    onConfigSelected: (selected: SolverConfigurationDTO) => void
}

const emptySolverConfig: SolverConfigurationDTO = {
    id: generateUUID(),
    name: "Nový",
    startDate: new Date().toISOString(),
    endDate: new Date().toISOString(),
    constraints: [],
    employees: [],
    createdAt: new Date().toISOString(),
    lastUpdated: new Date().toISOString(),
}

export function ConfigurationSelectMenu(props: Props) {

    const [isOpenSelectDialog, setIsOpenSelectDialog] = useState(false);
    const [isOpenImportDialog, setIsOpenImportDialog] = useState(false);

    function handleConfigSelected(metadata: SolverConfigurationMetadata) {
        SolverConfigurationEndpoint.getConfiguration(metadata.id).then(props.onConfigSelected)
    }

    return (
        <Card style={{justifyContent: "center"}}>
            <VerticalLayout
                theme={"spacing-l padding"}
                style={{
                    width: "100%",
                    justifyContent: "center",
                    alignItems: "center"
                }}>
                <h5>Začni plánovat</h5>
                <Button theme={buttonTheme}
                        style={buttonStyle}
                        onClick={() => props.onConfigSelected(emptySolverConfig)}
                >Nový</Button>
                <Button theme={buttonTheme}
                        style={buttonStyle}
                        onClick={() => setIsOpenSelectDialog(true)}
                >Načíst existující</Button>
                <Button theme={buttonTheme}
                        style={buttonStyle}
                        onClick={() => setIsOpenImportDialog(true)}
                >Importovat</Button>
                <ConfigSelectDialog
                    onConfigSelected={handleConfigSelected}
                    onOpenChanged={setIsOpenSelectDialog}
                    isOpen={isOpenSelectDialog}
                />
                <ConfigUploadDialog
                    isOpen={isOpenImportDialog}
                    onOpenChanged={setIsOpenImportDialog}
                    onImport={props.onConfigSelected}
                />
            </VerticalLayout>
        </Card>
    );
}
