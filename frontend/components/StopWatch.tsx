import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { CSSProperties, useEffect, useState } from "react";

type Props = {
  isRunning: boolean
  style?: CSSProperties
}
export function StopWatch(props: Props) {

  const [time, setTime] = useState(0);
  const hours = Math.floor(time / 3600)
  const minutes = Math.floor((time % 3600) / 60)
  const seconds = Math.floor(time % 60)

  useEffect(() => {
    let interval: ReturnType<typeof setInterval>
    if (props.isRunning) {
      interval = setInterval(() => {
        setTime(time => time + 1)
      }, 1000);
    }
    return () => clearInterval(interval)
  }, [time, props.isRunning]);

  useEffect(() => {
    if (props.isRunning) {
      setTime(0)
    }
  }, [props.isRunning]);

  return (
    <HorizontalLayout style={props.style}>
      {hours > 0 ? hours.toLocaleString("en-US", { minimumIntegerDigits: 2 }) + " : " : null}
      {minutes.toLocaleString("en-US", { minimumIntegerDigits: 2 })} : {seconds.toLocaleString("en-US", { minimumIntegerDigits: 2 })}
    </HorizontalLayout>
  );
}
