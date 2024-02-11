import { Dialog } from "@hilla/react-components/Dialog";
import { Card } from "Frontend/components/Card";
import { Select, SelectItem } from "@hilla/react-components/Select";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/lib/ConstraintType";
import { useState } from "react";
import { Button } from "@hilla/react-components/Button";
import { CardFooter } from "Frontend/components/CardFooter";
import { Constraint, defaultConstraints } from "Frontend/views/schedule/DefaultEmptyConstraints";

type Props = {
  isOpen: boolean
  onOpenChanged: (value: boolean) => void
  onNewConstraint: (value: Constraint) => void
  allowedConstraints?: ConstraintType[]
}

export function AddNewScheduleConstraintForm(props: Props) {

  const selectItem: SelectItem[] = Object.values(defaultConstraints)
    .filter(b =>
      props.allowedConstraints
        ? props.allowedConstraints.some(c => c === b.type)
        : true
    ).map(b => ({
      label: b.label,
      value: b.type
    }))

  const [selectedItem, setSelectedItem] = useState<ConstraintType>(selectItem[0].value as ConstraintType);

  function handleConfirm() {
    switch (selectedItem) {
      case ConstraintType.CONSECUTIVE_WORKING_DAYS:
        props.onNewConstraint(defaultConstraints.CONSECUTIVE_WORKING_DAYS.constraint)
        break
      case ConstraintType.SHIFT_FOLLOW_UP_RESTRICTION:
        props.onNewConstraint(defaultConstraints.SHIFT_FOLLOW_UP_RESTRICTION.constraint)
        break
      case ConstraintType.WORKERS_PER_SHIFT:
        props.onNewConstraint(defaultConstraints.WORKERS_PER_SHIFT.constraint)
        break
    }
  }

  return (
    <Dialog
      opened={props.isOpen}
      onOpenedChanged={e => props.onOpenChanged(e.detail.value)}
    >
      <Card>
        <Select
          style={{ width: "350px" }}
          items={selectItem}
          value={selectedItem}
          onChange={e => setSelectedItem(e.target.value as ConstraintType)}
        />
      </Card>
      <CardFooter>
        <Button onClick={handleConfirm}>Vybrat</Button>
      </CardFooter>
    </Dialog>
  );
}
