import { Cell, GridCell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import { CSSProperties, ReactNode, useContext } from "react";
import { FirstColumnCell } from "Frontend/views/schedule/components/schedulegrid/FirstColumnCell";
import { FirstRowCell } from "Frontend/views/schedule/components/schedulegrid/FirstRowCell";
import { CrudAction, dateToString } from "Frontend/util/utils";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import { ValidationContext } from "Frontend/views/schedule/components/validation/ScheduleValidationCtxProvider";
import EmployeeAssignmentDTO
  from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import IssueSeverity from "Frontend/generated/com/cocroachden/planner/solverconfiguration/validations/IssueSeverity";

type TeamColor = {
  [key: number]: string
}

const dayVocabulary: Record<number, string> = {
  1: "Po",
  2: "Út",
  3: "St",
  4: "Čt",
  5: "Pá",
  6: "So",
  0: "Ne"
}

const teamColors: TeamColor = {
  1 : "#4787ff",
  2 : "#ff5671",
  3 : "#7fff62",
  4 : "#b77dff",
  5 : "#ff8b57",
  6 : "#ffdd3d",
  7 : "#76e6ff",
  8 : "#848484"
}

export type Row = {
  owner: string
  rowTitle: ReactNode
  cells: Cell[]
  team?: number
}

type Props = {
  rows: Row[]
  onCellChanged?: (cell: Cell) => void
  onLeftClick?: (cell: Cell) => void
  onMouseOverCell?: (cell: Cell) => void
  onAssignmentAction: (action: CrudAction<Pick<EmployeeAssignmentDTO, "employeeId">>) => void
}

export function ScheduleGrid(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx)
  const validationCtx = useContext(ValidationContext)
  const items = mapToGridCells(props.rows);

  function mapToGridCells(rows: Row[]): ReactNode[] {
    const items: ReactNode[] = []
    rows.forEach((row, rowIndex) => {
      if (rowIndex === 0) {
        items.push(generateFirstRow(row))
      }
      const workerIssues = validationCtx.employeeIssueMap.get(row.owner) || []
      const severity = validationCtx.getSeverityOfIssues(workerIssues)
      items.push(renderCell(
        rowIndex + 2,
        1,
        <FirstColumnCell
          title={row.rowTitle}
          owner={row.owner}
          onAssignmentAction={props.onAssignmentAction}
          style={{
            backgroundColor: severity === IssueSeverity.ERROR ? "var(--lumo-error-color-50pct)"
              : severity === IssueSeverity.WARNING ? "var(--lumo-primary-color-50pct)"
                : cellColor(rowIndex + 2, 1, modeCtx.mode)
          }}
          readonly={modeCtx.mode !== ScheduleMode.EDIT}
          issues={workerIssues}
        />
      ))
      return row.cells
        .forEach((c) => {
          items.push(renderCell(
            rowIndex + 2,
            c.index + 2,
            <GridCell
              cell={c}
              onShiftChange={props.onCellChanged}
              onMouseOverCell={props.onMouseOverCell}
              onLeftClick={props.onLeftClick}
              backgroundColor={cellColor(rowIndex + 2, c.index + 2, modeCtx.mode, c.date)}
              readonly={modeCtx.mode !== ScheduleMode.EDIT}
              color={c.color}
            />
          ));
        })
    })
    return items
  }

  function generateFirstRow(row: Row): ReactNode[] {
    const items: ReactNode[] = []
    items.push(renderCell(
      1,
      1,
      <FirstColumnCell
        owner={row.owner}
        onAssignmentAction={() => {
        }}
        title={"Jmeno"}
        style={{ backgroundColor: cellColor(1, 1, modeCtx.mode) }}
        issues={[]}
      />
    ))
    row.cells.forEach(c => {
      const issues = validationCtx.dayIssueMap.get(dateToString(c.date)) || []
      const severity = validationCtx.getSeverityOfIssues(issues)
      items.push(
        renderCell(
          1,
          c.index + 2,
          <FirstRowCell
            title={c.date.getDate().toString()}
            hint={dateToString(c.date) + ", " + dayVocabulary[c.date.getDay()]}
            style={{
              backgroundColor: severity === IssueSeverity.ERROR ? "var(--lumo-error-color-50pct)"
                : severity === IssueSeverity.WARNING ? "var(--lumo-primary-color)"
                  : cellColor(1, c.index + 2, modeCtx.mode, c.date),
              position: "sticky",
              left: 0
            }}
            issues={issues}
          />
        ))
    })
    return items
  }

  return (
    <div style={{
      display: "grid",
      width: "100%",
    }}>
      {items}
    </div>
  )
}

function shadowIntensity(
  row: number,
  column: number,
  mode: ScheduleMode,
  date?: Date
): number {
  let shadowIntensity = 10
  if (date && isWeekend(date)) shadowIntensity += 20
  if (mode !== ScheduleMode.EDIT) shadowIntensity += 10
  if (row === 1) shadowIntensity += 30
  if (column === 1 && row > 1) shadowIntensity += 10
  return shadowIntensity
}

function cellColor(
  row: number,
  column: number,
  mode: ScheduleMode,
  date?: Date
) {
  return "var(--lumo-shade-" + shadowIntensity(row, column, mode, date) + "pct)"
}

function renderCell(
  row: number,
  column: number,
  content: ReactNode
) {

  const cellStyle: CSSProperties = { gridRow: row, gridColumn: column }

  if (row == 1 && column == 1) {
    cellStyle["position"] = "sticky"
    cellStyle["top"] = 0
    cellStyle["left"] = 0
    cellStyle["backgroundColor"] = "var(--lumo-base-color)"
    cellStyle["zIndex"] = 3
  } else if (row === 1) {
    cellStyle["position"] = "sticky"
    cellStyle["zIndex"] = 2
    cellStyle["top"] = 0
    cellStyle["backgroundColor"] = "var(--lumo-base-color)"
  } else if (column == 1) {
    cellStyle["position"] = "sticky"
    cellStyle["zIndex"] = 1
    cellStyle["left"] = 0
    cellStyle["backgroundColor"] = "var(--lumo-base-color)"
  }

  return (
    <div
      key={"r" + row.toString() + "c" + column.toString()}
      style={cellStyle}
      className={(column == 1 && row > 1) ? "hoverable" : undefined}
    >
      {content}
    </div>
  );
}

function isWeekend(date: Date): boolean {
  return date.getDay() === 0 || date.getDay() === 6
}


