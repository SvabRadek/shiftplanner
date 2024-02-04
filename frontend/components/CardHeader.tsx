import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { CSSProperties, ReactNode } from "react";

type Props = {
  children?: ReactNode
  style?: CSSProperties
}

export function CardHeader(props: Props) {
  return (
    <HorizontalLayout
      style={{
        paddingBottom: props.style?.paddingBottom ?? "var(--lumo-size-xs)",
        width: props.style?.width ?? "100%",
        justifyContent: props.style?.justifyContent ?? "flex-start"
      }}
      theme={"spacing"}
    >
      {props.children}
    </HorizontalLayout>
  );
}
