import {CrudAction, CRUDActions} from "Frontend/util/utils";
import {Tooltip} from "@hilla/react-components/Tooltip";
import {CSSProperties, ReactNode} from "react";
import EmployeeId from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeId";
import EmployeeValidationIssue
    from "Frontend/generated/com/cocroachden/planner/constraint/validations/employee/EmployeeValidationIssue";
import AssignedEmployeeDTO from "Frontend/generated/com/cocroachden/planner/solver/api/AssignedEmployeeDTO";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {GridProperties} from "Frontend/views/schedule/components/schedulegrid/GridProperties";

const defaultStyle: CSSProperties = {
    cursor: "pointer",
    display: "flex",
    userSelect: "none",
    width: GridProperties.cellWidth * 3,
    height: GridProperties.cellHeight,
    border: "solid",
    borderColor: "var(--lumo-tint-20pct)",
    borderWidth: "1px",
    justifyContent: "space-between",
    alignItems: "center",
    backgroundColor: "var(--lumo-shade-5pct)",
    paddingLeft: 10,
    paddingRight: 10,
}

type Props = {
    owner: EmployeeId,
    title: ReactNode
    onAssignmentAction: (action: CrudAction<Pick<AssignedEmployeeDTO["employee"], "id">>) => void
    issues: EmployeeValidationIssue[]
    readonly?: boolean
    style?: CSSProperties
}

export function FirstColumnCell(props: Props) {

    const cellStyle = Object.assign({}, defaultStyle, props.style)

    function handleLeftClick() {
        props.onAssignmentAction({
            type: CRUDActions.READ,
            payload: props.owner
        })
    }

    return (
        <div
            id={"headerCell" + props.owner}
            onClick={handleLeftClick}
            style={cellStyle}
        >
            <VerticalLayout theme={"spacing"} style={{width: "100%"}}>
                {props.title}
                {props.issues.length > 0
                    && <Tooltip
                        for={"headerCell" + props.owner}
                        text={props.issues.map(i => i.issue).join("\n")}
                        position={"top-end"}
                    />}
            </VerticalLayout>
        </div>
    )
}
