import {CSSProperties, ReactNode} from "react";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {combineStyles} from "Frontend/util/utils";

type Props = {
    children?: ReactNode
    style?: CSSProperties
    theme?: string
}

export function Card(props: Props) {
    const style = combineStyles({
        display: "flex",
        boxShadow: "0 4px 8px 0 rgba(26, 26, 26, 0.3)",
        transition: "0.3s",
        borderRadius: "var(--lumo-border-radius-m)"
    }, props.style)

    return (
        <div style={style}>
            <VerticalLayout
                theme={props.theme ?? "spacing-xs padding"}
                style={{width: "100%"}}
            >
                {props.children}
            </VerticalLayout>
        </div>
    );
}
