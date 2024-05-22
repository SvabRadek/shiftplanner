import { Cell, DisplayMode, GridCell } from "Frontend/views/schedule/components/schedulegrid/GridCell";
import { ReactNode, useContext } from "react";
import { GridNameCell } from "Frontend/views/schedule/components/schedulegrid/GridNameCell";
import { GridHeaderCell } from "Frontend/views/schedule/components/schedulegrid/GridHeaderCell";
import { CrudAction, stupidDateToDate, stupidDateToString } from "Frontend/util/utils";
import StupidDate from "Frontend/generated/com/cocroachden/planner/lib/StupidDate";
import { ScheduleMode, ScheduleModeCtx } from "Frontend/views/schedule/ScheduleModeCtxProvider";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import ValidatorIssue from "Frontend/generated/com/cocroachden/planner/solver/constraints/validator/ValidatorIssue";
import IssueSeverity from "Frontend/generated/com/cocroachden/planner/solver/constraints/validator/IssueSeverity";
import WorkerId from "Frontend/generated/com/cocroachden/planner/lib/WorkerId";
import { ValidationContext } from "Frontend/views/schedule/components/validation/ScheduleValidationCtxProvider";

export type Row = {
  owner: WorkerId
  displayName: string
  cells: Cell[]
  issues?: ValidatorIssue[]
}

type Props = {
  rows: Row[]
  onCellChanged?: (cell: Cell) => void
  onLeftClick?: (cell: Cell) => void
  onMouseOverCell?: (cell: Cell) => void
  onEmployeeAction: (action: CrudAction<Pick<EmployeeRecord, "id">>) => void
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

function shadowIntensity(
  row: number,
  column: number,
  mode: ScheduleMode,
  date?: StupidDate
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
  date?: StupidDate
) {
  return "var(--lumo-shade-" + shadowIntensity(row, column, mode, date) + "pct)"
}

export function ScheduleGrid(props: Props) {

  const modeCtx = useContext(ScheduleModeCtx)
  const validationCtx = useContext(ValidationContext)

  const items = mapToGridCells(props.rows);

  function generateFirstRow(row: Row): ReactNode[] {
    const items: ReactNode[] = []
    items.push(renderCell(
      1,
      1,
      <GridNameCell
        owner={row.owner}
        onEmployeeAction={props.onEmployeeAction}
        title={"Jmeno"}
        backgroundColor={cellColor(1, 1, modeCtx.mode)}
        disableContextMenu={true}
        issues={[]}
      />
    ))
    row.cells.forEach(c => {
      const issues = validationCtx.dayIssueMap.get(stupidDateToString(c.date)) || []
      const severity = validationCtx.getSeverityOfIssues(issues)
      items.push(
        renderCell(
          1,
          c.index + 2,
          <GridHeaderCell
            title={c.date.day.toString()}
            hint={stupidDateToString(c.date) + ", " + dayVocabulary[stupidDateToDate(c.date).getDay()]}
            backgroundColor={
            severity === IssueSeverity.ERROR ? "var(--lumo-error-color-50pct)"
              : severity === IssueSeverity.WARNING ? "var(--lumo-primary-color)"
                : cellColor(1, c.index + 2, modeCtx.mode, c.date)
            }
            issues={issues}
          />
        ))
    })
    return items
  }

  function mapToGridCells(rows: Row[]): ReactNode[] {
    const items: ReactNode[] = []
    rows.forEach((row, rowIndex) => {
      if (rowIndex === 0) {
        items.push(generateFirstRow(row))
      }
      const workerIssues = validationCtx.workerIssueMap.get(row.owner.id) || []
      const severity = validationCtx.getSeverityOfIssues(workerIssues)
        items.push(renderCell(
        rowIndex + 2,
        1,
        <GridNameCell
          title={row.displayName}
          owner={row.owner}
          onEmployeeAction={props.onEmployeeAction}
          backgroundColor={
            severity === IssueSeverity.ERROR ? "var(--lumo-error-color-50pct)"
            : severity === IssueSeverity.WARNING ? "var(--lumo-primary-color-50pct)"
                : cellColor(rowIndex + 2, 1, modeCtx.mode)}
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
              color={c.displayMode === DisplayMode.PATTERN
                ? "var(--lumo-contrast-30pct)"
                : undefined
              }
            />
          ));
        })
    })
    return items
  }

  return (
    <div style={{
      display: "grid",
      width: "100%",
      justifyContent: "start"
    }}>
      {items}
    </div>
  )
}

function renderCell(
  row: number,
  column: number,
  content: ReactNode
) {
  return (
    <div
      key={"r" + row.toString() + "c" + column.toString()}
      style={{ gridRow: row, gridColumn: column }}
    >
      {content}
    </div>
  )
}

function isWeekend(stupidDate: StupidDate): boolean {
  const date = stupidDateToDate(stupidDate)
  return date.getDay() === 0 || date.getDay() === 6
}


