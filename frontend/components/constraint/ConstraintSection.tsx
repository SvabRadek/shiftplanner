import {CrudAction, CRUDActions} from "Frontend/util/utils";
import ConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintDTO";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {ReactNode} from "react";
import {Button} from "@hilla/react-components/Button";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {Icon} from "@hilla/react-components/Icon";

type Props<C extends ConstraintDTO> = {
    constraintsOfType: C[]
    onConstraintAction: (action: CrudAction<ConstraintDTO>) => void
    title: string,
    defaultNewConstraint: () => ConstraintDTO
    renderer: (constraint: C) => ReactNode
    owner?: string
}

export function ConstraintSection<C extends ConstraintDTO>(props: Props<C>) {

    function handleCreateNew() {
        props.onConstraintAction({
            type: CRUDActions.CREATE,
            payload: props.defaultNewConstraint()
        })
    }

    return (
        <VerticalLayout style={{width: "100%"}}>
            <VerticalLayout theme={"spacing"} style={{width: "100%"}}>
                <HorizontalLayout theme={"spacing"} style={{ alignItems: "baseline" }}>
                    <Button
                        theme={"icon"}
                        style={{ marginTop: "var(--lumo-size-xs)" }}
                        onClick={handleCreateNew}>
                        <Icon icon={"vaadin:plus"}/>
                    </Button>
                    <h6>{props.title}</h6>
                </HorizontalLayout>
            </VerticalLayout>
            {props.constraintsOfType.map(c => props.renderer(c))}
        </VerticalLayout>
    );
}
