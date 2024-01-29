import { useEffect, useState } from "react";
import { Grid } from "@hilla/react-components/Grid";
import { GridColumn } from "@hilla/react-components/GridColumn";
import PlannerConfigurationMetaDataDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationMetaDataDTO";

type Props = {
  configList: PlannerConfigurationMetaDataDTO[]
  onSelectionChanged: (value: PlannerConfigurationMetaDataDTO) => void
}

export function PlannerConfigList(props: Props) {

  const [selectedItems, setSelectedItems] = useState<PlannerConfigurationMetaDataDTO[]>([])

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
      <GridColumn header={"Id"} path={"id"}/>
    </Grid>
  );
}
