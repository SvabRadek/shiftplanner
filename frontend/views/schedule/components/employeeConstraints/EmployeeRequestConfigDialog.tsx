import { Dialog } from "@hilla/react-components/Dialog";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { TextField } from "@hilla/react-components/TextField";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import {
  ShiftCountConstraintForm
} from "Frontend/views/schedule/components/employeeConstraints/ShiftCountConstraintForm";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { useState } from "react";
import { Tabs } from "@hilla/react-components/Tabs";
import { Tab } from "@hilla/react-components/Tab";
import { TabSheet } from "@hilla/react-components/TabSheet";
import { Icon } from "@hilla/react-components/Icon";
import { areShiftPerScheduleSame, generateUUID } from "Frontend/util/utils";
import WorkerId from "Frontend/generated/com/cocroachden/planner/lib/WorkerId";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/lib/ConstraintType";
import { Card } from "Frontend/components/Card";
import { CardFooter } from "Frontend/components/CardFooter";

export type EmployeeConfigModel = {
  workerId: string
  shiftsPerScheduleRequests: ShiftsPerScheduleRequestDTO[]
}

type Props = {
  employee?: EmployeeRecord
  isOpen: boolean
  shiftsPerScheduleRequests: ShiftsPerScheduleRequestDTO[]
  onSave: (value: EmployeeConfigModel) => void
  onOpenChanged: (value: boolean) => void
  readonly?: boolean
}

export function EmployeeRequestConfigDialog(props: Props) {

  if (!props.employee) return null
  const [employeeConfigModel, setEmployeeConfigModel] = useState<EmployeeConfigModel>({
    workerId: props.employee.workerId,
    shiftsPerScheduleRequests: props.shiftsPerScheduleRequests
  })

  function handleClose() {
    props.onOpenChanged(false)
    setEmployeeConfigModel({
      ...employeeConfigModel,
      shiftsPerScheduleRequests: props.shiftsPerScheduleRequests
    })
  }

  function handleSave() {
    props.onSave(employeeConfigModel)
  }

  function addNewShiftPerScheduleRequest(workerId: string) {
    setEmployeeConfigModel(prevState => ({
      ...prevState,
      shiftsPerScheduleRequests: [
        ...prevState.shiftsPerScheduleRequests,
        generateNewUniqueShiftsPerSchedule(workerId, prevState.shiftsPerScheduleRequests.map(s => s.targetShift))
      ]
    }))
  }

  function removeShiftPerScheduleRequest(workerId: WorkerId, shift: WorkShifts) {
    setEmployeeConfigModel(prevState => ({
      ...prevState,
      shiftsPerScheduleRequests: prevState.shiftsPerScheduleRequests.filter(r => !areShiftPerScheduleSame(r, {
        owner: workerId,
        targetShift: shift
      }))
    }))
  }

  function handleRequestUpdate(previous: WorkShifts, updatedRequest: ShiftsPerScheduleRequestDTO) {
    setEmployeeConfigModel(prevState => ({
      ...prevState,
      shiftsPerScheduleRequests: prevState.shiftsPerScheduleRequests.map(r => {
        if (!areShiftPerScheduleSame(r, { targetShift: previous, owner: updatedRequest.owner })) return r
        return updatedRequest
      })
    }));
  }

  return (
    <Dialog
      header-title={"Konfigurace zamestance"}
      opened={props.isOpen}
      onOpenedChanged={e => {
        props.onOpenChanged(e.detail.value)
      }}
    >
      <VerticalLayout
        style={{ width: "600px", minHeight: "300px" }}
        theme={"spacing"}
      >
        <Card style={{ width: "100%" }}>
          <HorizontalLayout theme={"spacing"}>
            <TextField value={props.employee.firstName} readonly/>
            <TextField value={props.employee.lastName} readonly/>
          </HorizontalLayout>
        </Card>
        <Card style={{ width: "100%" }}>
          <TabSheet style={{ width: "100%" }}>
            <Tabs slot={"tabs"}>
              <Tab id={"smeny-tab"}>Smeny</Tab>
              <Tab id={"pattern-tab"}>Rozvrh</Tab>
            </Tabs>
            <div {...{ tab: "smeny-tab" }}>
              {
                employeeConfigModel.shiftsPerScheduleRequests.length === 0
                  ? <div style={{ paddingTop: "var(--lumo-size-xs)" }}/>
                  : employeeConfigModel.shiftsPerScheduleRequests.map(request => {
                    return (
                      <ShiftCountConstraintForm
                        key={request.owner.workerId + request.targetShift}
                        request={request}
                        onChange={handleRequestUpdate}
                        onRemove={removeShiftPerScheduleRequest}
                        excludedShifts={employeeConfigModel.shiftsPerScheduleRequests.map(r => r.targetShift)}
                        readonly={props.readonly}
                      />
                    )
                  })
              }
              <Button
                theme={"small"}
                style={{ marginTop: "var(--lumo-size-xs)" }}
                disabled={props.readonly}
                onClick={() => addNewShiftPerScheduleRequest(props.employee!.workerId)}>
                <Icon icon={"vaadin:plus"} slot={"prefix"}/>
                Pridat
              </Button>
            </div>
            <div {...{ tab: "pattern-tab" }}>
              <div>To be done...</div>
            </div>
          </TabSheet>
        </Card>
      </VerticalLayout>
      <CardFooter>
        <Button onClick={handleClose}>Zrusit</Button>
        <Button disabled={props.readonly} onClick={handleSave}>Ulozit</Button>
      </CardFooter>
    </Dialog>
  );
}

function generateNewUniqueShiftsPerSchedule(workerId: string, excludeShifts: WorkShifts[]): ShiftsPerScheduleRequestDTO {

  const allowedShifts = Object.values(WorkShifts).filter(val => !excludeShifts.some(s => s === val))

  return {
    id: generateUUID(),
    type: ConstraintType.SHIFT_PER_SCHEDULE,
    owner: { workerId: workerId },
    targetShift: allowedShifts[0],
    hardMin: 0,
    softMin: 0,
    minPenalty: 1,
    softMax: 0,
    maxPenalty: 1,
    hardMax: 0
  }
}
