import {Button, ButtonProps} from "@hilla/react-components/Button";
import {CSSProperties} from "react";

const defaultStyle: CSSProperties = {
    minWidth: "150px"
}

type Props = {} & ButtonProps

export function SecondaryButton(props: Props) {

    const finalStyle = Object.assign({}, defaultStyle, props.style);

    return (
        <Button theme={"secondary"} style={finalStyle} {...props} >
            {props.children}
        </Button>
    );
}
