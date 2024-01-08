import { Grid } from "@hilla/react-components/Grid";
import { GridColumn } from "@hilla/react-components/GridColumn";
import PlannerConfigurationRecord
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationRecord";
import { useEffect, useState } from "react";
import { ConstraintRequestService } from "Frontend/generated/endpoints";
import SpecificShiftRequestResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/ConstraintRequestService/SpecificShiftRequestResponse";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import { HorizontalLayout } from "@hilla/react-components/HorizontalLayout";
import { Checkbox } from "@hilla/react-components/Checkbox";
import { Select } from "@hilla/react-components/Select";
import { workShiftBindings } from "Frontend/views/schedule/WorkShiftBindigs";

type BrushState = {
  selected: boolean,
  shift: WorkShifts
}

type GridRow = {
  ownerName: string
  cells: Record<number, Cell>
}

type Cell = {
  shift: WorkShifts
  index: number
  owner: string
}

type Props = {
  config: PlannerConfigurationRecord
}

const brushSelectItems = Object.values(workShiftBindings)
  .map(bind => ({ label: bind.fullText, value: bind.shift }))

export function ScheduleGrid({ config }: Props) {

  const [shiftRequests, setShiftRequests] = useState<SpecificShiftRequestResponse[]>([])
  const [brushState, setBrushState] = useState<BrushState>(
    { selected: false, shift: WorkShifts.ANY }
  )
  const [gridRows, setGridRows] = useState<Record<string, GridRow>>({})

  const startDateObj = new Date(config.startDate)
  const dayIndexes = getDistanceInDays(startDateObj, new Date(config.endDate))

  useEffect(() => {
    ConstraintRequestService.getSpecificShiftRequests(config.id).then(setShiftRequests)
  }, []);

  useEffect(() => {
    setGridRows(mapConfigToGridRows(config))
  }, [config]);

  function mapConfigToGridRows(config: PlannerConfigurationRecord): Record<string, GridRow> {
    const rows: Record<string, GridRow> = {}
    config.workers
      .map(w => {
        const cells: Record<number, Cell> = {}
        dayIndexes
          .map(day => ({ shift: WorkShifts.ANY, index: day, owner: w.workerId! }))
          .forEach(cell => cells[cell.index] = cell)

        shiftRequests
          .filter(r => r.owner === w.workerId)
          .forEach(r => {
            const rDate = new Date(r.date.year, r.date.month, r.date.day)
            const index = getDistanceInDaysNumeric(startDateObj, rDate)
            const cell: Cell = { shift: r.requestedShift, index, owner: w.workerId! }
            if (index >= 0) cells[cell.index] = cell
          })
        return {
          ownerName: w.workerId!,
          cells: cells
        }
      }).forEach(gr => rows[gr.ownerName] = gr)
    return rows
  }

  function updateCell(cell: Cell) {
    setGridRows(oldValue => {
      const targetRow = oldValue[cell.owner]
      targetRow.cells[cell.index] = cell
      return {
        ...oldValue,
        [cell.owner]: targetRow
      }
    })
  }

  function handleCellClick(cell: Cell) {
    const nextShift = brushState.selected
      ? brushState.shift
      : getNextWorkShift(cell.shift)
    updateCell({
        ...cell,
        shift: nextShift
      }
    )
  }

  return (
    <>
      <HorizontalLayout theme={"spacing-s"} style={{ alignItems: "center" }}>
        <Select
          items={brushSelectItems}
          value={brushState.shift}
          onChange={(e) => {
            setBrushState(old => ({ ...old, shift: e.target.value as WorkShifts }))
          }}
        />
        <Checkbox
          label={"Stetec"}
          checked={brushState.selected}
          onClick={() => setBrushState(old => ({ ...old, selected: !brushState.selected }))}
        />
      </HorizontalLayout>
      <Grid items={Object.values(gridRows)} theme={"compact"}>
        <GridColumn width={"200px"} header={"Jmeno"} path={"ownerName"}/>
        {dayIndexes.map(dayIndex => (
          <GridColumn key={dayIndex} width={"50px"} header={(dayIndex + 1).toString()}>
            {({ item: { cells, ownerName } }) => (
              <div
                key={dayIndex + ownerName}
                className={"flex-auto"}
                style={{ width: 50, height: 40, backgroundColor: "gray", userSelect: "none" }}
                onClick={() => handleCellClick(cells[dayIndex])}
              >
                <span>{workShiftBindings[cells[dayIndex].shift].symbol}</span>
              </div>
            )}
          </GridColumn>
        ))}
      </Grid>
    </>
  );
}

function getNextWorkShift(shift: WorkShifts): WorkShifts {
  const nextShiftIndex = workShiftBindings[shift].indexOfNext
  return Object.values(workShiftBindings)[nextShiftIndex].shift
}

function getDistanceInDays(startDate: Date, endDate: Date): number[] {
  const diffTime = Math.abs(endDate.valueOf() - startDate.valueOf());
  const dayCount = diffTime / (1000 * 60 * 60 * 24)
  const dayIndexes: number[] = []
  for (let i = 0; i < dayCount; i++) {
    dayIndexes.push(i)
  }
  return dayIndexes
}

function getDistanceInDaysNumeric(startDate: Date, endDate: Date): number {
  const diffTime = Math.abs(endDate.valueOf() - startDate.valueOf());
  return diffTime / (1000 * 60 * 60 * 24)
}
