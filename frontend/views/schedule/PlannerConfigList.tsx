import { useEffect, useState } from "react";
import PlannerConfigurationMetaData
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationMetaData";
import { PlannerConfigurationService } from "Frontend/generated/endpoints";
import { Grid } from "@hilla/react-components/Grid";
import { GridColumn } from "@hilla/react-components/GridColumn";

export function PlannerConfigList() {

  const [configMetaData, setConfigMetaData] = useState<PlannerConfigurationMetaData[]>([])

  useEffect(() => {
    PlannerConfigurationService.getMetaData().then(setConfigMetaData)
  }, []);

  return (
    <>
      <Grid items={configMetaData} style={{ height: 200 }}>
        <GridColumn header={"Jmeno"} path={"name"}/>
        <GridColumn header={"Od"} path={"startDate"}/>
        <GridColumn header={"Do"} path={"endDate"}/>
        <GridColumn header={"Id"} path={"id"}/>
      </Grid>
    </>
  );
}
