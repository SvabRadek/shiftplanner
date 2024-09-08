import { Dialog } from "@hilla/react-components/Dialog";
import { Button } from "@hilla/react-components/Button";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { GridColumn } from "@hilla/react-components/GridColumn";
import { Grid } from "@hilla/react-components/Grid";
import { Icon } from "@hilla/react-components/Icon";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/repository/EmployeeRecord";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeDTO";
import AssignedEmployeeDTO from "Frontend/generated/com/cocroachden/planner/solver/api/AssignedEmployeeDTO";
import "@vaadin/icons";

type Props = {
  employees: EmployeeDTO[]
  onAssignmentAdd: (value: Omit<AssignedEmployeeDTO, "index">) => void
  onOpenChanged: (value: boolean) => void
  isOpen: boolean
}

export function AddEmployeeDialog(props: Props) {

  return (
    <>
      <Dialog
        header-title={"Vyber zamestnance"}
        opened={props.isOpen}
        onOpenedChanged={e => {
          props.onOpenChanged(e.detail.value)
        }}
      >
        <VerticalLayout style={{ justifyContent: "space-between", minWidth: "75vw", minHeight: "50vh" }}>
          {props.employees.length > 0 ?
            <Grid items={props.employees} style={{ height: 200 }}>
              <GridColumn header={"Jmeno"} path={"firstName"}/>
              <GridColumn header={"Prijmeni"} path={"lastName"}/>
              <GridColumn header={"Akce"} flexGrow={0}>
                {(props1) => (
                  <Button theme={"icon"}
                          onClick={() => props.onAssignmentAdd?.({ employee: props1.item as EmployeeDTO, weight: 1 })}>
                    <Icon icon={"vaadin:plus"}/>
                  </Button>
                )}
              </GridColumn>
            </Grid>
            : <h6 style={{ paddingTop: "100px", alignSelf: "center" }} >Vsichni zamestnanci jsou jiz prirazeni</h6>
          }
          <HorizontalLayout style={{ width: "100%", justifyContent: "end" }} theme={"padding spacing"}>
            <Button onClick={() => props.onOpenChanged(false)}>Zavrit</Button>
          </HorizontalLayout>
        </VerticalLayout>
      </Dialog>
    </>
  );
}
