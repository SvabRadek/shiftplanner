import { createContext, FC, ReactNode, useState } from "react";

export enum ScheduleMode {
  READONLY,
  EDIT,
  CALCULATING
}

type ScheduleModeCtxType = {
  mode: ScheduleMode
  setMode: (value: ScheduleMode) => void
}

export const ScheduleModeCtx = createContext<ScheduleModeCtxType>({
  mode: ScheduleMode.READONLY,
  setMode: value => {}
});
export const ScheduleModeProvider: FC<{ children?: ReactNode | undefined }> = props => {
  const [mode, setMode] = useState<ScheduleMode>(ScheduleMode.READONLY);

  return (
    <ScheduleModeCtx.Provider value={{
      mode,
      setMode: value => setMode(() => value)
    }}>
      {props.children}
    </ScheduleModeCtx.Provider>
  );
};
