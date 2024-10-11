import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {useState} from "react";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import {ConfigurationSelectMenu} from "Frontend/views/schedule/components/configurationselect/ConfigurationSelectMenu";
import {ScheduleEditor} from "Frontend/views/schedule/components/scheduleedit/ScheduleEditor";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import {CustomEmployeeEndpoint} from "Frontend/generated/endpoints";
import {CrudAction, CRUDActions} from "Frontend/util/utils";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";

type EmployeeDialogProps = {
    employeeId: string | undefined,
    isOpen: boolean
}

export function ScheduleView2() {

    const [employeeDialog, setEmployeeDialog] = useState<EmployeeDialogProps>({ employeeId: undefined, isOpen: false });
    const [solverConfiguration, setSolverConfiguration] = useState<SolverConfigurationDTO | undefined>();
    const [employees, setEmployees] = useState<EmployeeDTO[]>([]);

    async function handleConfigSelection(selected: SolverConfigurationDTO) {
        const ids = selected.employees.map(a => a.employeeId);
        const fetchedEmployees = await CustomEmployeeEndpoint.findEmployees(ids);
        setEmployees(fetchedEmployees)
        setSolverConfiguration(selected)
    }


    return (
        <VerticalLayout style={{width: "100%", height: "100%", justifyContent: "center", alignItems: "center"}}>
            {solverConfiguration
                ? <ScheduleEditor employees={employees} configuration={solverConfiguration}/>
                : <ConfigurationSelectMenu onConfigSelected={handleConfigSelection}/>
            }
        </VerticalLayout>
    );
}
