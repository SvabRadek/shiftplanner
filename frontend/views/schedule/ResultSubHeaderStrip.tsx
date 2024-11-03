import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {Button} from "@hilla/react-components/Button";
import {Icon} from "@hilla/react-components/Icon";
import {StopWatch} from "Frontend/components/StopWatch";
import {ProgressBar} from "@hilla/react-components/ProgressBar";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import "@vaadin/icons";
import {CachedSolutions} from "Frontend/views/schedule/ScheduleView2";

type Props = {
    onExportToExcel: () => void
    onCachedSolutionsChanged: (cachedSolutions: CachedSolutions) => void
    cacheSize: number,
    stopped: boolean
    cachedSolutions?: CachedSolutions
}

export function ResultSubHeaderStrip(props: Props) {
    const areSomeResultsCached = props.cachedSolutions
        ? props.cachedSolutions.solutions.length > 0
        : false
    const solutions = props.cachedSolutions
        ? props.cachedSolutions.solutions
        : []
    const currentIndex = props.cachedSolutions
        ? props.cachedSolutions.currentIndex
        : 0

    function handleMoveByOne(value: -1 | 1) {
        props.onCachedSolutionsChanged({
            solutions,
            currentIndex: currentIndex + value
        })
    }

    return (
        <VerticalLayout style={{paddingTop: 10, width: "100%"}}>
            <HorizontalLayout theme={"spacing"} style={{alignItems: "center"}}>
                <Button disabled={currentIndex === 0}
                        onClick={() => handleMoveByOne(-1)}
                        theme={"small icon"}>
                    <Icon style={{transform: "rotate(180deg)"}} icon={"vaadin:play"}/>
                </Button>
                <span style={{userSelect: "none"}}>
          Řešení: {areSomeResultsCached ? currentIndex + 1 : "-"}
          </span>
                <span style={{userSelect: "none"}}>
          Skóre: {areSomeResultsCached ? solutions[currentIndex].resultScore : "-"}
          </span>
                <Button disabled={currentIndex >= solutions.length - 1}
                        onClick={() => handleMoveByOne(1)}
                        theme={"small icon"}><Icon icon={"vaadin:play"}/>
                </Button>
                <StopWatch style={{
                    borderLeft: "solid",
                    paddingLeft: 10,
                    borderWidth: 1,
                    borderColor: "var(--lumo-contrast-20pct)"
                }} isRunning={!props.stopped}></StopWatch>
                <Button theme={"small"}
                        disabled={!areSomeResultsCached}
                        onClick={props.onExportToExcel}>Export</Button>
            </HorizontalLayout>
            {!props.stopped && <ProgressBar style={{marginBottom: 0}} indeterminate></ProgressBar>}
        </VerticalLayout>
    );
}
