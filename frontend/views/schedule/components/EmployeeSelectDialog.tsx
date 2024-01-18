import { Dialog } from "@hilla/react-components/Dialog";
import { useContext, useEffect, useState } from "react";
import { Button } from "@hilla/react-components/Button";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import { RequestCtx } from "Frontend/views/schedule/components/schedulegrid/RequestCtxProvider";
import { GridColumn } from "@hilla/react-components/GridColumn";
import { Grid } from "@hilla/react-components/Grid";

type Props = {
  onEmployeeSelected?: (value: EmployeeRecord) => void
  onOpenChanged: (value: boolean) => void
  isOpen: boolean
}

export function EmployeeSelectDialog(props: Props) {

  const requestCtx = useContext(RequestCtx)
  const [employeesToShow, setEmployeesToShow] = useState<EmployeeRecord[]>([])
  const [selectedItems, setSelectedItems] = useState<EmployeeRecord[]>([])

  useEffect(() => {
    const unassignedEmployees = requestCtx.allEmployees
      .filter(employee => {
        const isAlreadyAssigned = requestCtx.request?.workers.find(w => w.workerId === employee.workerId)
        return !isAlreadyAssigned
      })
    setEmployeesToShow(unassignedEmployees)
  }, [requestCtx.request?.workers]);

  function confirmSelection() {
    if (props.onEmployeeSelected && selectedItems.length > 0) {
      props.onEmployeeSelected(selectedItems[0])
    }
  }

  return (
    <>
      <Dialog
        header-title={"Vyber zamestnance"}
        opened={props.isOpen}
        onOpenedChanged={e => {
          props.onOpenChanged(e.detail.value)
        }}
      >
        <VerticalLayout style={{ minWidth: "700px", minHeight: "400px" }}>
          <Grid
            items={employeesToShow}
            style={{ height: 200 }}
            selectedItems={selectedItems}
            onActiveItemChanged={(e) => {
              const item = e.detail.value
              setSelectedItems(item ? [item] : [])
            }}
          >
            <GridColumn header={"Jmeno"} path={"firstName"}/>
            <GridColumn header={"Prijmeni"} path={"lastName"}/>
          </Grid>
          <HorizontalLayout style={{ width: "100%", justifyContent: "end" }} theme={"padding spacing"}>
            <Button onClick={() => props.onOpenChanged(false)}>Zrusit</Button>
            <Button disabled={selectedItems.length === 0} onClick={confirmSelection}>Vybrat</Button>
          </HorizontalLayout>
        </VerticalLayout>
      </Dialog>
    </>
  );
}
