import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { ConfigSelectDialog } from "Frontend/views/schedule/components/ConfigSelectDialog";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Subscription } from "@hilla/frontend";
import { useContext, useEffect, useState } from "react";
import { ResultCache } from "Frontend/views/schedule/ScheduleView";
import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { Card } from "Frontend/components/Card";
import { ResultSubHeaderStrip } from "Frontend/views/schedule/ResultSubHeaderStrip";
import { ValidationIssuesDialog } from "Frontend/views/schedule/components/validation/ValidationIssuesDialog";
import { Tooltip } from "@hilla/react-components/Tooltip";
import { ValidationContext } from "Frontend/views/schedule/components/validation/ScheduleValidationCtxProvider";
import { SolverConfigurationEndpoint } from "Frontend/generated/endpoints";
import { GridDisplayMode } from "Frontend/views/schedule/components/schedulegrid/ScheduleGridContainer";
import "@vaadin/icons";
import SolverConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import SolverConfigurationMetadata
  from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationMetadata";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/SolverSolutionDTO";

type Props = {
  onStopCalculation: () => void
  onStartCalculation: () => void
  onValidateRequest: () => void
  onSave: () => void
  onUpdate: () => void
  onCancel: () => void
  onClearCache: () => void
  onExportToExcel: () => void
  onResultSelectionChanged: (value: 1 | -1) => void
  onConfigSelected: (id: string) => void
  gridDisplayMode: GridDisplayMode
  onGridDisplayModeChange: (mode: GridDisplayMode) => void
  cacheSize: number
  resultSubscription: Subscription<SolverSolutionDTO> | undefined
  request: SolverConfigurationDTO | undefined
  resultCache: ResultCache
}

export function HeaderStrip(props: Props) {
  const [configMetaData, setConfigMetaData] = useState<SolverConfigurationMetadata[]>([]);
  const [isConfigSelectDialogOpen, setIsConfigSelectDialogOpen] = useState(false);
  const [isIssuesDialogOpen, setIsIssuesDialogOpen] = useState(false);
  const modeCtx = useContext(ScheduleModeCtx);
  const validationCtx = useContext(ValidationContext);

  useEffect(() => {
    if (isConfigSelectDialogOpen) {
      SolverConfigurationEndpoint.getMetaData().then(setConfigMetaData)
    }
  }, [isConfigSelectDialogOpen])

  async function handleConfigAction(action: CrudAction<SolverConfigurationMetadata>) {
    switch (action.type) {
      case CRUDActions.DELETE:
        await SolverConfigurationEndpoint.delete(action.payload.id)
        SolverConfigurationEndpoint.getMetaData().then(setConfigMetaData)
        break
      case CRUDActions.READ:
        props.onConfigSelected(action.payload.id)
    }
  }

  return (
    <Card style={{ width: "100%" }}>
      <HorizontalLayout theme={"spacing"}>
        <ValidationIssuesDialog
          isOpen={isIssuesDialogOpen}
          onOpenChanged={setIsIssuesDialogOpen}
        />
        {validationCtx.containsIssues &&
            <Button onClick={() => setIsIssuesDialogOpen(true)} theme={"error primary icon"}>
                <Tooltip slot={"tooltip"} text={"Zadání obsahuje chyby!"}/>
                <Icon icon={"vaadin:exclamation"}/>
            </Button>
        }
        {props.resultSubscription ?
          <Button onClick={props.onStopCalculation} theme={"primary"}>
            <Icon icon={"vaadin:stop"}></Icon>
            Stop
          </Button>
          : <Button onClick={props.onStartCalculation}
                    disabled={modeCtx.mode === ScheduleMode.EDIT || !props.request}
                    theme={"primary"}>
            <Icon icon={"vaadin:play"}/>
            Vypočítat
          </Button>
        }
        <Button theme={"icon primary"} onClick={() => setIsConfigSelectDialogOpen(true)}>
          <Icon icon={"vaadin:cog"} slot={"prefix"}/>
          Vybrat konfiguraci
        </Button>
        <ConfigSelectDialog
          configMetaData={configMetaData}
          onOpenChanged={value => setIsConfigSelectDialogOpen(value)}
          isOpen={isConfigSelectDialogOpen}
          onConfigAction={handleConfigAction}
        />
        {props.request && <Button
            disabled={modeCtx.mode === ScheduleMode.EDIT || modeCtx.mode === ScheduleMode.CALCULATING}
            onClick={() => {
              modeCtx.setMode(ScheduleMode.EDIT)
            }}>
            Upravit</Button>
        }
        {props.request && <Button
            disabled={modeCtx.mode === ScheduleMode.CALCULATING}
            onClick={props.onValidateRequest}>
            Zkontrolovat</Button>
        }
        {props.request && <Button
            disabled={modeCtx.mode !== ScheduleMode.EDIT}
            theme={"secondary"}
            onClick={props.onUpdate}>Uložit</Button>
        }
        {props.request && <Button
            disabled={modeCtx.mode !== ScheduleMode.EDIT}
            theme={"secondary"}
            onClick={props.onSave}>Uložit jako nový</Button>
        }
        {props.request && <Button
            disabled={modeCtx.mode !== ScheduleMode.EDIT}
            theme={"secondary"}
            onClick={props.onCancel}>Zrušit</Button>
        }
        {props.resultCache.results.length > 0 && !props.resultSubscription &&
            <HorizontalLayout theme={"spacing"}>
                <Button
                    theme={"secondary"}
                    onClick={() => props.onGridDisplayModeChange(
                      props.gridDisplayMode === GridDisplayMode.RESULT ? GridDisplayMode.PLANNING : GridDisplayMode.RESULT
                    )}
                >
                  {props.gridDisplayMode === GridDisplayMode.PLANNING ? "Zobrazit výsledky" : "Zobrazit plán"}
                </Button>
                <Button theme={"secondary"} onClick={props.onClearCache}>
                    Vyčistit výsledky
                </Button>
            </HorizontalLayout>
        }
      </HorizontalLayout>
      {(props.resultSubscription || props.resultCache.results.length > 0)
        && <ResultSubHeaderStrip
              onExportToExcel={props.onExportToExcel}
              resultCache={props.resultCache}
              cacheSize={props.cacheSize}
              resultSubscription={props.resultSubscription}
              resultSelectionChanged={props.onResultSelectionChanged}
          />}
    </Card>

  );
}
