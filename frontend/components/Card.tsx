import { CSSProperties, ReactNode } from "react";
import { VerticalLayout } from "@hilla/react-components/VerticalLayout";

type Props = {
  children: ReactNode
  style?: CSSProperties
}
export function Card(props: Props) {
  return (
    <div
      style={{
        ...props.style,
        display: props.style?.display?? "flex",
        boxShadow: props.style?.boxShadow?? "0 4px 8px 0 rgba(26, 26, 26, 0.3)",
        transition: props.style?.transition?? "0.3s",
        borderRadius: props.style?.borderRadius?? "var(--lumo-border-radius-m)"
      }}
    >
      <VerticalLayout
        theme={"spacing-xs padding"}
        style={{ width: "100%" }}
      >
        {props.children}
      </VerticalLayout>
    </div>
  );
}
