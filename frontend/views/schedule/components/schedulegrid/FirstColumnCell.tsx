import {CrudAction, CRUDActions} from "Frontend/util/utils";
import {Tooltip} from "@hilla/react-components/Tooltip";
import {CSSProperties, ReactNode} from "react";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {GridProperties} from "Frontend/views/schedule/components/schedulegrid/GridProperties";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import EmployeeValidationIssueDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/validations/employee/EmployeeValidationIssueDTO";

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
    owner: string,
    title: ReactNode
    onAssignmentAction: (action: CrudAction<Pick<EmployeeAssignmentDTO, "employeeId">>) => void
    issues: EmployeeValidationIssueDTO[]
    readonly?: boolean
    style?: CSSProperties
}

export function FirstColumnCell(props: Props) {

    const cellStyle = Object.assign({}, defaultStyle, props.style)

    function handleLeftClick() {
        props.onAssignmentAction({
            type: CRUDActions.READ,
            payload: { employeeId: props.owner }
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
