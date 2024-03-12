import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { ConfigSelectDialog } from "Frontend/views/schedule/components/ConfigSelectDialog";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Subscription } from "@hilla/frontend";
import ScheduleResultDTO from "Frontend/generated/com/cocroachden/planner/solver/ScheduleResultDTO";
import { useContext, useEffect, useState } from "react";
import PlannerConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationDTO";
import { ResultCache } from "Frontend/views/schedule/ScheduleView";
import PlannerConfigurationMetaDataDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationMetaDataDTO";
import { CrudAction } from "Frontend/util/utils";
import { PlannerConfigurationEndpoint } from "Frontend/generated/endpoints";
import { Card } from "Frontend/components/Card";
import { ResultSubHeaderStrip } from "Frontend/views/schedule/ResultSubHeaderStrip";

type Props = {
  onStopCalculation: () => void
  onStartCalculation: () => void
  onConfigAction: (action: CrudAction<PlannerConfigurationMetaDataDTO>) => void
  onValidateRequest: () => void
  onSave: () => void
  onUpdate: () => void
  onCancel: () => void
  onClearCache: () => void
  onExportToExcel: () => void
  onResultSelectionChanged: (value: 1 | -1) => void
  cacheSize: number
  resultSubscription: Subscription<ScheduleResultDTO> | undefined
  request: PlannerConfigurationDTO | undefined
  resultCache: ResultCache
}

export function HeaderStrip(props: Props) {
  const [configMetaData, setConfigMetaData] = useState<PlannerConfigurationMetaDataDTO[]>([]);
  const [isConfigSelectDialogOpen, setIsConfigSelectDialogOpen] = useState(false);
  const modeCtx = useContext(ScheduleModeCtx);

  useEffect(() => {
    if (isConfigSelectDialogOpen) {
      PlannerConfigurationEndpoint.getMetaData().then(setConfigMetaData)
    }
  }, [isConfigSelectDialogOpen])

  return (
    <Card style={{ width: "100%" }}>
      <HorizontalLayout theme={"spacing"}>
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
          onConfigAction={props.onConfigAction}
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
            <Button theme={"secondary"} onClick={props.onClearCache}>
                Vyčistit výsledky
            </Button>
        }
      </HorizontalLayout>
      {props.resultSubscription && <ResultSubHeaderStrip
          onExportToExcel={props.onExportToExcel}
          resultCache={props.resultCache}
          cacheSize={props.cacheSize}
          resultSubscription={props.resultSubscription}
          resultSelectionChanged={props.onResultSelectionChanged}
      />}
    </Card>

  );
}
