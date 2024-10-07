import {Dialog} from "@hilla/react-components/Dialog";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {useRef, useState} from "react";
import {importFromExcel, ImportResult} from "Frontend/util/excel";
import {Button} from "@hilla/react-components/Button";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {CustomEmployeeEndpoint} from "Frontend/generated/endpoints";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import {TextField} from "@hilla/react-components/TextField";
import {DatePicker} from "@hilla/react-components/DatePicker";
import {FormLayout} from "@hilla/react-components/FormLayout";
import {generateUUID} from "Frontend/util/utils";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintType";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import ConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintDTO";
import ShiftsPerScheduleConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleConstraintDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";

type Props = {
    isOpen: boolean
    onOpenChanged: (value: boolean) => void
    onImport: (configuration: SolverConfigurationDTO) => void
}

const responsiveSteps = [
    {minWidth: '0', columns: 1},
    {minWidth: '500px', columns: 2},
];

export function ConfigUploadDialog(props: Props) {

    const [importedResult, setImportedResult] = useState<ImportResult | null>(null);
    const [configName, setConfigName] = useState("");
    const [startDate, setStartDate] = useState(new Date().toISOString().split("T")[0]);
    const fileInputRef = useRef<HTMLInputElement | null>(null);

    async function handleConfirmation() {
        if (!importedResult) return
        const employeeAssignments: EmployeeAssignmentDTO[] = []
        const constraints: ConstraintDTO[] = []
        let maxOffset = 0
        for (let [index, employee] of importedResult.employees.entries()) {
            let existing = await CustomEmployeeEndpoint.findByName(employee.firstName, employee.lastName)
            if (!existing) {
                existing = {
                    id: await CustomEmployeeEndpoint.save({
                        id: "random",
                        firstName: employee.firstName,
                        lastName: employee.lastName
                    }),
                    firstName: employee.firstName,
                    lastName: employee.lastName,
                }
            }
            const typedStartDate = new Date(Date.parse(startDate))
            importedResult.requestedShifts
                .filter(a => a.row === employee.row)
                .map(a => {
                    const constraintDate = new Date(typedStartDate)
                    constraintDate.setDate(typedStartDate.getDate() + a.offset)
                    if (a.offset > maxOffset) {
                        maxOffset = a.offset
                    }
                    return {
                        id: generateUUID(),
                        type: ConstraintType.REQUESTED_SHIFT_CONSTRAINT,
                        requestedShift: a.shift,
                        owner: existing?.id!,
                        date: constraintDate.toISOString().split("T")[0],
                    };
                }).forEach(a => constraints.push(a))
            importedResult.requestedShiftCount
                .filter(r => r.row === employee.row)
                .map(r => {
                  const result: ShiftsPerScheduleConstraintDTO = {
                      id: generateUUID(),
                      type: ConstraintType.SHIFTS_PER_SCHEDULE,
                      owner: existing?.id!,
                      targetShift: WorkShifts.WORKING_SHIFTS,
                      hardMax: r.count + r.deviation,
                      maxPenalty: 1,
                      softMax: r.count,
                      softMin: r.count,
                      minPenalty: 1,
                      hardMin: r.count - r.deviation
                  }
                  return result
                }).forEach(r => constraints.push(r))
            employeeAssignments.push({employeeId: existing.id, index, weight: 1})
        }
        const typedStartDate = new Date(Date.parse(startDate))
        const endDate = new Date(typedStartDate)
        endDate.setDate(typedStartDate.getDate() + maxOffset)
        const config = {
            id: "any",
            name: configName,
            employees: employeeAssignments,
            startDate: startDate,
            endDate: endDate.toISOString().split("T")[0],
            createdAt: "",
            lastUpdated: "",
            constraints
        };
        props.onImport(config)
        props.onOpenChanged(false)
    }

    function handleCancel() {
        props.onOpenChanged(false)
    }

    function handleImportClick() {
        if (fileInputRef.current) {
            fileInputRef.current.click();
        }
    }

    const handleFileInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files && event.target.files.length > 0) {
            const selectedFile = event.target.files[0]
            const reader = new FileReader();
            reader.onloadend = (e) => {
                const binary = e.target?.result;
                if (binary) {
                    setImportedResult(importFromExcel(binary))
                }
            }
            reader.onerror = () => {
                new Error("Error reading excel file!")
            }
            reader.readAsBinaryString(selectedFile!)
        }
    }

    return (
        <Dialog
            header-title={"Importuj Konfiguraci"}
            opened={props.isOpen}
            onOpenedChanged={e => {
                props.onOpenChanged(e.detail.value)
            }}
        >
            <VerticalLayout theme={"padding spacing"} style={{width: "50vw", minHeight: "400px", justifyContent: "space-between"}}>
                <FormLayout responsiveSteps={responsiveSteps}>
                    <TextField label={"Nazev"} value={configName} onValueChanged={e => setConfigName(e.detail.value)}></TextField>
                    <DatePicker label={"Zacina od"} value={startDate} onValueChanged={e => setStartDate(e.detail.value)}/>
                    <Button {...{colspan: 2}} theme={"primary"} style={{width: "100%"}}
                            onClick={handleImportClick}>{"Import"}</Button>
                </FormLayout>
                <input
                    type="file"
                    ref={fileInputRef}
                    onChange={handleFileInputChange}
                    style={{display: 'none'}}
                />
                <HorizontalLayout theme={"spacing"} style={{width: "100%", justifyContent: "end"}}>
                    <Button onClick={handleConfirmation} theme={"primary"}>Ok</Button>
                    <Button onClick={handleCancel} theme={"secondary"}>Cancel</Button>
                </HorizontalLayout>
            </VerticalLayout>
        </Dialog>
    );
}
