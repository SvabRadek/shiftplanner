import { Dialog } from "@hilla/react-components/Dialog";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";
import { ValidationDayIssueCard } from "Frontend/components/validation/ValidationDayIssueCard";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Button } from "@hilla/react-components/Button";
import { ValidationWorkerIssueCard } from "Frontend/components/validation/ValidationWorkerIssueCard";
import { useContext } from "react";
import { ValidationContext } from "Frontend/components/validation/ScheduleValidationCtxProvider";
import "@vaadin/icons";

type Props = {
  isOpen: boolean
  onOpenChanged: (value: boolean) => void
}

export function ValidationIssuesDialog(props: Props) {

  const validationCtx = useContext(ValidationContext);

  return (
    <Dialog
      opened={props.isOpen}
      onOpenedChanged={e => props.onOpenChanged(e.detail.value)}
    >
      <VerticalLayout theme={"spacing"} style={{
        width: "75vw",
        maxWidth: "800px",
        maxHeight: "75vh"
      }}>
        {validationCtx.dayIssues
          .map(issue => <ValidationDayIssueCard
            key={issue.issue + issue.localDate}
            issue={issue}/>
          )}
        {validationCtx.employeeIssues
          .map(issue => <ValidationWorkerIssueCard
            key={issue.issue + issue.employeeId}
            issue={issue!}/>
          )}
        <HorizontalLayout style={{ width: "100%", justifyContent: "end" }}>
          <Button onClick={() => props.onOpenChanged(false)}>Zavrit</Button>
        </HorizontalLayout>
      </VerticalLayout>
    </Dialog>
  );
}
