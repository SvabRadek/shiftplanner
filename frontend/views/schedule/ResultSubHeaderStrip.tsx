import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { StopWatch } from "Frontend/components/StopWatch";
import { ProgressBar } from "@hilla/react-components/ProgressBar";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { ResultCache } from "Frontend/views/schedule/ScheduleView";
import { Subscription } from "@hilla/frontend";
import ScheduleResultDTO from "Frontend/generated/com/cocroachden/planner/solver/ScheduleResultDTO";

type Props = {
  resultSubscription?: Subscription<ScheduleResultDTO>
  onExportToExcel: () => void
  resultCache: ResultCache
  cacheSize: number,
  resultSelectionChanged: (value: 1 | -1) => void
}

export function ResultSubHeaderStrip(props: Props) {
  const areSomeResultsCached = props.resultCache.results.length > 0
  return (
    <VerticalLayout style={{ paddingTop: 10, width: "100%" }}>
      <HorizontalLayout theme={"spacing"} style={{ alignItems: "center" }}>
        {areSomeResultsCached &&
            <Button disabled={props.resultCache.selectedIndex === 0}
                    onClick={() => props.resultSelectionChanged(-1)}
                    theme={"small icon"}>
                <Icon style={{ transform: "rotate(180deg)" }} icon={"vaadin:play"}/>
            </Button>
        }
        <span style={{ userSelect: "none" }}>
          Řešení: {areSomeResultsCached ? props.resultCache.results[props.resultCache.selectedIndex].resultIndex : "-"}
          </span>
        <span
          style={{ userSelect: "none" }}>Skóre: {areSomeResultsCached ? props.resultCache.results[props.resultCache.selectedIndex].resultScore : "-"}
          </span>
        {areSomeResultsCached &&
            <Button disabled={props.resultCache.selectedIndex >= props.resultCache.results.length - 1}
                    onClick={() => props.resultSelectionChanged(1)}
                    theme={"small icon"}><Icon icon={"vaadin:play"}/></Button>
        }
        <StopWatch style={{
          borderLeft: "solid",
          paddingLeft: 10,
          borderWidth: 1,
          borderColor: "var(--lumo-contrast-20pct)"
        }} isRunning={props.resultSubscription !== undefined}></StopWatch>
        <Button theme={"small"}
                disabled={!areSomeResultsCached}
                onClick={props.onExportToExcel}>Export</Button>
      </HorizontalLayout>
      {props.resultSubscription && <ProgressBar style={{ marginBottom: 0 }} indeterminate></ProgressBar>}
    </VerticalLayout>
  );
}
