import {CrudAction, CRUDActions} from "Frontend/util/utils";
import {Card} from "Frontend/components/Card";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {NumberField} from "@hilla/react-components/NumberField";
import {ScheduleMode, ScheduleModeCtx} from "Frontend/views/schedule/ScheduleModeCtxProvider";
import {useContext} from "react";
import {Icon} from "@hilla/react-components/Icon";
import {Button} from "@hilla/react-components/Button";
import {Checkbox} from "@hilla/react-components/Checkbox";
import EvenShiftDistributionRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/EvenShiftDistributionRequestDTO";
import "@vaadin/icons";

type Props = {
    request: EvenShiftDistributionRequestDTO,
    onAction: (action: CrudAction<EvenShiftDistributionRequestDTO>) => void
}

export function EvenDistributionConstraintForm(props: Props) {

    const {mode} = useContext(ScheduleModeCtx);

    function handleUpdate(update: Partial<EvenShiftDistributionRequestDTO>) {
        props.onAction({
            type: CRUDActions.UPDATE,
            payload: {
                ...props.request,
                ...update
            }
        })
    }

    function handleRemove() {
        props.onAction({
            type: CRUDActions.DELETE,
            payload: props.request
        })
    }

    return (
        <Card style={{width: "100%"}}>
            <HorizontalLayout style={{width: "100%"}}>
                <HorizontalLayout theme={"spacing"} style={{width: "100%", alignItems: "baseline"}}>
                    <Checkbox
                        label={"Rozprostřít směny rovnoměrně napříč rozvrhem"}
                        checked={props.request.distributeShiftsEvenlyThroughoutSchedule}
                        onCheckedChanged={e => props.onAction({
                            type: CRUDActions.UPDATE,
                            payload: {
                                ...props.request,
                                distributeShiftsEvenlyThroughoutSchedule: e.detail.value
                            }
                        })}
                    />
                    <NumberField
                        label={"Pokuta za deviaci od průmeru v rámci týdne"}
                        style={{width: 250}}
                        readonly={mode !== ScheduleMode.EDIT && props.request.distributeShiftsEvenlyThroughoutSchedule}
                        theme={"small"}
                        value={props.request.penaltyForDeviationFromWeeksAverage.toString()}
                        onChange={e => handleUpdate({
                            penaltyForDeviationFromWeeksAverage: Number.parseInt(e.target.value)
                        })}
                    />
                </HorizontalLayout>
                <Button theme={"small icon"}
                        style={{alignSelf: "start"}}
                        onClick={handleRemove}
                        disabled={mode !== ScheduleMode.EDIT}>
                    <Icon icon={"vaadin:trash"}></Icon>
                </Button>
            </HorizontalLayout>
        </Card>
    );
}
