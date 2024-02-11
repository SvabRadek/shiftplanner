import { Card } from "Frontend/components/Card";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/lib/ConstraintType";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import { Icon } from "@hilla/react-components/Icon";
import { ReactNode, useState } from "react";

type Props = {
  type: ConstraintType
  children: ReactNode
  onDelete: () => void
}

export function ConstraintCard(props: Props) {

  const [editMode, setEditMode] = useState(false);

  return (
    <Card style={{ width: "100%", transition: "0.3" }}>
      <HorizontalLayout theme={"spacing"} style={{ width: "100%", alignItems: "center", justifyContent: "space-between" }}>
        {props.type}
        <HorizontalLayout theme={"spacing"}>
          {
            editMode ?
              <Button theme={"icon small"} onClick={() => setEditMode(false)}>
                <Icon icon={"vaadin:save"}></Icon>
              </Button>
              :
              <Button theme={"icon small"} onClick={() => setEditMode(true)}>
                <Icon icon={"vaadin:edit"}></Icon>
              </Button>
          }
          <Button theme={"icon small"}>
            <Icon icon={"vaadin:close"}></Icon>
          </Button>
        </HorizontalLayout>
      </HorizontalLayout>
      {editMode && props.children}
    </Card>
  );
}
