import {GridProperties} from "Frontend/views/schedule/components/schedulegrid/GridProperties";
import {CSSProperties, ReactNode} from "react";

type Props = {
    children: ReactNode
    style?: CSSProperties
    onClick?: () => void
}

const defaultStyle: CSSProperties = {
    width: GridProperties.cellWidth * 2,
    height: GridProperties.cellHeight
}

export function NameTag(props: Props) {

    const style = Object.assign({}, defaultStyle, props.style)

    return (
        <div
            onClick={props.onClick}
            style={style}
        >
            {props.children}
        </div>
    );
}