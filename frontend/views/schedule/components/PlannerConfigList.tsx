import { useEffect, useState } from "react";
import { Grid } from "@hilla/react-components/Grid";
import { GridColumn } from "@hilla/react-components/GridColumn";
import { CrudAction, CRUDActions } from "Frontend/util/utils";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import SolverConfigurationMetadata
  from "Frontend/generated/com/cocroachden/planner/solver/repository/SolverConfigurationMetadata";
import "@vaadin/icons";

type Props = {
  configList: SolverConfigurationMetadata[]
  onSelectionChanged: (value: SolverConfigurationMetadata) => void
  onAction: (action: CrudAction<SolverConfigurationMetadata>) => void
}

export function PlannerConfigList(props: Props) {

  const [selectedItems, setSelectedItems] = useState<SolverConfigurationMetadata[]>([])

  useEffect(() => {
    selectedItems.length > 0 && props.onSelectionChanged?.(selectedItems[0])
  }, [selectedItems]);

  return (
    <Grid
      items={props.configList}
      style={{ height: 200 }}
      selectedItems={selectedItems}
      onActiveItemChanged={(e) => {
        const item = e.detail.value
        setSelectedItems(item ? [item] : [])
      }}
    >
      <GridColumn header={"Jmeno"} path={"name"}/>
      <GridColumn header={"Od"} path={"startDate"}/>
      <GridColumn header={"Do"} path={"endDate"}/>
      <GridColumn header={"Vytvořeno"} path={"createdAt"}/>
      <GridColumn header={"Id"} path={"id"}/>
      <GridColumn header={"Akce"} flexGrow={0}>
        {(row) =>
          <Button theme={"small icon"} onClick={() => props.onAction({
            type: CRUDActions.DELETE,
            payload: row.item
          })}>
            <Icon icon={"vaadin:trash"}></Icon>
          </Button>}
      </GridColumn>
    </Grid>
  );
}
