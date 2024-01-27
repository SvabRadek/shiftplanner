import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { NumberField } from "@hilla/react-components/NumberField";



export function ShiftCountConstraintForm() {
  return (
    <VerticalLayout>
      <HorizontalLayout>
        <NumberField></NumberField>
      </HorizontalLayout>
    </VerticalLayout>
  );
}
