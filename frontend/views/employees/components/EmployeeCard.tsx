import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import {Card} from "Frontend/components/Card";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {TextField} from "@hilla/react-components/TextField";
import {useState} from "react";
import {Button} from "@hilla/react-components/Button";
import {Icon} from "@hilla/react-components/Icon";
import {CrudAction} from "Frontend/util/utils";

type Props = {
    employee: EmployeeDTO
    onEmployeeAction: (action: CrudAction<EmployeeDTO>) => void
}


export function EmployeeCard(props: Props) {

    const [isInEdit, setIsInEdit] = useState(false);

    return (
        <Card>
            <HorizontalLayout theme={"spacing"} style={{ width: "100%", justifyContent: "end" }}>
                <Button theme={"small icon"}>
                    <Icon icon={"vaadin:trash"}></Icon>
                </Button>
            </HorizontalLayout>
            <HorizontalLayout theme={"spacing"} style={{width: "100%"}}>
                <TextField label={"Jmeno"} value={props.employee.firstName} readonly={!isInEdit}/>
                <TextField label={"Prijmeni"} value={props.employee.lastName} readonly={!isInEdit}/>
            </HorizontalLayout>
        </Card>
    );
}
