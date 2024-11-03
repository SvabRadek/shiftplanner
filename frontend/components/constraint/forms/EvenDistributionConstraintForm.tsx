import {CrudAction, CRUDActions} from "Frontend/util/utils";
import {Card} from "Frontend/components/Card";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {NumberField} from "@hilla/react-components/NumberField";
import {Icon} from "@hilla/react-components/Icon";
import {Button} from "@hilla/react-components/Button";
import {Checkbox} from "@hilla/react-components/Checkbox";
import "@vaadin/icons";
import EvenShiftDistributionConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/EvenShiftDistributionConstraintDTO";

type Props = {
    constraint: EvenShiftDistributionConstraintDTO,
    onAction: (action: CrudAction<EvenShiftDistributionConstraintDTO>) => void
    readonly: boolean
}

export function EvenDistributionConstraintForm(props: Props) {

    function handleUpdate(update: Partial<EvenShiftDistributionConstraintDTO>) {
        props.onAction({
            type: CRUDActions.UPDATE,
            payload: {
                ...props.constraint,
                ...update
            }
        })
    }

    function handleRemove() {
        props.onAction({
            type: CRUDActions.DELETE,
            payload: props.constraint
        })
    }

    return (
        <Card style={{width: "100%"}}>
            <HorizontalLayout style={{width: "100%"}}>
                <HorizontalLayout theme={"spacing"} style={{width: "100%", alignItems: "baseline"}}>
                    <Checkbox
                        label={"Rozprostřít směny rovnoměrně napříč rozvrhem"}
                        checked={props.constraint.distributeShiftsEvenlyThroughoutSchedule}
                        onCheckedChanged={e => props.onAction({
                            type: CRUDActions.UPDATE,
                            payload: {
                                ...props.constraint,
                                distributeShiftsEvenlyThroughoutSchedule: e.detail.value
                            }
                        })}
                    />
                    <NumberField
                        label={"Pokuta za deviaci od průmeru v rámci týdne"}
                        style={{width: 250}}
                        readonly={props.readonly && props.constraint.distributeShiftsEvenlyThroughoutSchedule}
                        theme={"small"}
                        value={props.constraint.penaltyForDeviationFromWeeksAverage.toString()}
                        onChange={e => handleUpdate({
                            penaltyForDeviationFromWeeksAverage: Number.parseInt(e.target.value)
                        })}
                    />
                </HorizontalLayout>
                <Button theme={"small icon"}
                        style={{alignSelf: "start"}}
                        onClick={handleRemove}
                        disabled={props.readonly}>
                    <Icon icon={"vaadin:trash"}></Icon>
                </Button>
            </HorizontalLayout>
        </Card>
    );
}
