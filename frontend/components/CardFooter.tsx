import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { CSSProperties, ReactNode } from "react";

type Props = {
  children?: ReactNode
  style?: CSSProperties
}

export function CardFooter(props: Props) {
  return (
    <HorizontalLayout
      style={{
        paddingTop: props.style?.paddingTop ?? "var(--lumo-size-xs)",
        width: props.style?.width ?? "100%",
        justifyContent: props.style?.justifyContent ?? "flex-end"
      }}
      theme={"spacing"}
    >
      {props.children}
    </HorizontalLayout>
  );
}
