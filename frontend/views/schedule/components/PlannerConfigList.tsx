import { useEffect, useState } from "react";
import { PlannerConfigurationService } from "Frontend/generated/endpoints";
import { Grid } from "@hilla/react-components/Grid";
import { GridColumn } from "@hilla/react-components/GridColumn";
import PlannerConfigurationMetaDataDTO
  from "Frontend/generated/com/cocroachden/planner/plannerconfiguration/PlannerConfigurationMetaDataDTO";

type Props = {
  onSelectionChanged: (value: PlannerConfigurationMetaDataDTO) => void
}

export function PlannerConfigList(props: Props) {

  const [configMetaData, setConfigMetaData] = useState<PlannerConfigurationMetaDataDTO[]>([])
  const [selectedItems, setSelectedItems] = useState<PlannerConfigurationMetaDataDTO[]>([])

  useEffect(() => {
    PlannerConfigurationService.getMetaData().then(setConfigMetaData)
  }, [])

  useEffect(() => {
    selectedItems.length > 0 && props.onSelectionChanged?.(selectedItems[0])
  }, [selectedItems]);

  return (
    <Grid
      items={configMetaData}
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
