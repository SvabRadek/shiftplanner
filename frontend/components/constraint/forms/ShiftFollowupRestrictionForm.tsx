import {Card} from "Frontend/components/Card";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {NumberField} from "@hilla/react-components/NumberField";
import {useContext} from "react";
import {ScheduleMode, ScheduleModeCtx} from "Frontend/views/schedule/ScheduleModeCtxProvider";
import {ShiftSelect} from "Frontend/components/ShiftSelect";
import {CrudAction, CRUDActions} from "Frontend/util/utils";
import {Icon} from "@hilla/react-components/Icon";
import {Button} from "@hilla/react-components/Button";
import {CardFooter} from "Frontend/components/CardFooter";
import "@vaadin/icons";
import ShiftFollowupRestrictionConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionConstraintDTO";

type Props = {
    constraint: ShiftFollowupRestrictionConstraintDTO
    onAction: (action: CrudAction<ShiftFollowupRestrictionConstraintDTO>) => void
    readonly: boolean
}

export function ShiftFollowupRestrictionForm(props: Props) {

    const modeCtx = useContext(ScheduleModeCtx);

    function handleUpdate(value: Partial<ShiftFollowupRestrictionConstraintDTO>) {
        props.onAction({
            type: CRUDActions.UPDATE,
            payload: {
                ...props.constraint,
                ...value
            }
        })
    }

    return (
        <Card style={{width: "100%"}}>
            <HorizontalLayout theme={"spacing"}>
                <ShiftSelect
                    readonly={props.readonly}
                    theme={"small"}
                    label={"Prvni smena"}
                    selectedShift={props.constraint.firstShift}
                    onSelect={e => handleUpdate({
                        firstShift: e
                    })}/>
                <ShiftSelect
                    readonly={props.readonly}
                    theme={"small"}
                    label={"Nasledujici smena"}
                    selectedShift={props.constraint.forbiddenFollowup}
                    onSelect={e => handleUpdate({
                        forbiddenFollowup: e
                    })}/>
                <NumberField
                    theme={"small"}
                    style={{width: "50px"}}
                    label={"Pokuta"}
                    value={props.constraint.penalty.toString()}
                    readonly={props.readonly}
                    onChange={e => handleUpdate({penalty: Number.parseInt(e.target.value)})}
                />
            </HorizontalLayout>
            <CardFooter style={{paddingTop: 0}}>
                <Button onClick={() => props.onAction({type: CRUDActions.DELETE, payload: props.constraint})}
                        disabled={props.readonly}
                        theme={"small icon"}>
                    <Icon icon={"vaadin:trash"}/>
                </Button>
            </CardFooter>
        </Card>
    );
}
