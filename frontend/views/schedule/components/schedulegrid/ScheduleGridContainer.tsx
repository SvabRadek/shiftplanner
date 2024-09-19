import {useMemo, useState} from "react";
import {CrudAction, dateToString, stringToDate} from "Frontend/util/utils";
import {Row, ScheduleGrid} from "Frontend/views/schedule/components/schedulegrid/ScheduleGrid";
import {Cell} from "Frontend/views/schedule/components/schedulegrid/GridCell";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import RequestedShiftConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/RequestedShiftConstraintDTO";
import ShiftPatternConstraintDTO from "Frontend/generated/com/cocroachden/planner/constraint/ShiftPatternConstraintDTO";
import TeamAssignmentConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/TeamAssignmentConstraintDTO";
import ShiftsPerScheduleConstraintDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleConstraintDTO";
import EmployeeAssignmentDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/ConstraintType";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/SolverSolutionDTO";

export enum GridDisplayMode {
    RESULT,
    PLANNING
}

type TeamColor = {
    [key: number]: string
}

const teamColors: TeamColor = {
    1: "#4787ff",
    2: "#ff5671",
    3: "#7fff62",
    4: "#b77dff",
    5: "#ff8b57",
    6: "#ffdd3d",
    7: "#76e6ff",
    8: "#848484"
}

export type PlainEmployeeId = number
export type Index = number

type CellShiftAndColor = {
    color: string | undefined
    shift: WorkShifts
}

type Props = {
    request: SolverConfigurationDTO
    employees: EmployeeDTO[]
    shiftPatterns: ShiftPatternConstraintDTO[]
    shiftRequests: RequestedShiftConstraintDTO[]
    teamAssignments: TeamAssignmentConstraintDTO[]
    shiftPerScheduleRequests: ShiftsPerScheduleConstraintDTO[]
    onAssignmentAction: (action: CrudAction<EmployeeAssignmentDTO>) => void
    onShiftRequestsChanged?: (changedRequests: Omit<RequestedShiftConstraintDTO, "id">[]) => void
    displayMode: GridDisplayMode
    result?: SolverSolutionDTO
}

type Highlight = {
    originCell: Cell | undefined
    lastCell: Cell | undefined
}

export function ScheduleGridContainer(props: Props) {
    const [highlight, setHighlight] = useState<Highlight>({originCell: undefined, lastCell: undefined})

    function handleCellLeftClick(cell: Cell) {
        if (highlight.originCell === undefined) {
            setHighlight(() => ({originCell: cell, lastCell: cell}))
        } else {
            handleGroupShiftChange(highlight.originCell, highlight.lastCell!)
            setHighlight(() => ({originCell: undefined, lastCell: undefined}))
        }
    }

    function handleCellOnMouseOver(cell: Cell) {
        if (!highlight.originCell) return
        setHighlight(previous => ({...previous, lastCell: cell}))
    }

    function handleShiftChange(updatedCell: Cell) {
        props.onShiftRequestsChanged?.([
            {
                type: ConstraintType.REQUESTED_SHIFT_CONSTRAINT,
                owner: updatedCell.employeeId,
                date: dateToString(updatedCell.date),
                requestedShift: updatedCell.shift
            }
        ])
    }

    function handleGroupShiftChange(originCell: Cell, endCell: Cell) {
        const originDate = originCell.index < endCell.index ? originCell.date : endCell.date
        const lowEnd = originCell.index < endCell.index ? originCell.index : endCell.index
        const highEnd = originCell.index > endCell.index ? originCell.index : endCell.index
        const requests: Omit<RequestedShiftConstraintDTO, "id">[] = []
        for (let i = lowEnd; i <= highEnd; i++) {
            const index = i - lowEnd
            const requestDate = new Date(originDate)
            requestDate.setDate(requestDate.getDate() + index)
            requests.push({
                    type: ConstraintType.REQUESTED_SHIFT_CONSTRAINT,
                    owner: originCell.employeeId,
                    requestedShift: originCell.shift,
                    date: dateToString(requestDate)
                } as Omit<RequestedShiftConstraintDTO, "id">
            )
        }
        props.onShiftRequestsChanged?.(requests)
    }

    const shiftRequestMap = useMemo(() => {
        const map = new Map<string, RequestedShiftConstraintDTO>()
        props.shiftRequests.forEach(r => map.set(r.date + r.owner, r))
        return map
    }, [props.shiftRequests])

    const shiftPatternMap = useMemo(() => {
        const map = new Map<string, ShiftPatternConstraintDTO>()
        props.shiftPatterns.forEach(r => map.set(r.owner, r))
        return map
    }, [props.shiftPatterns])

    return (
        <ScheduleGrid
            rows={
                createRows(
                    props.request,
                    props.employees,
                    shiftRequestMap,
                    shiftPatternMap,
                    props.shiftPerScheduleRequests,
                    props.teamAssignments,
                    highlight,
                    props.displayMode,
                    props.result
                )
            }
            onCellChanged={handleShiftChange}
            onMouseOverCell={handleCellOnMouseOver}
            onLeftClick={handleCellLeftClick}
            onAssignmentAction={a => props.onAssignmentAction({
                type: a.type,
                payload: props.request.employees.find(e => e.employeeId === a.payload.employeeId)!
            })}
        />
    );
}

function getDistanceInDays(startDate: Date, endDate: Date): Index[] {
    const diffTime = Math.abs(endDate.valueOf() - startDate.valueOf());
    const dayCount = diffTime / (1000 * 60 * 60 * 24)
    const dayIndexes: number[] = []
    for (let i = 0; i <= dayCount; i++) {
        dayIndexes.push(i)
    }
    return dayIndexes
}

function createRows(
    request: SolverConfigurationDTO,
    employees: EmployeeDTO[],
    shiftRequests: Map<string, RequestedShiftConstraintDTO>,
    shiftPatterns: Map<string, ShiftPatternConstraintDTO>,
    shiftPerSchedule: ShiftsPerScheduleConstraintDTO[],
    teamAssignments: TeamAssignmentConstraintDTO[],
    highlightInfo: Highlight,
    mode: GridDisplayMode,
    results?: SolverSolutionDTO
): Row[] {
    const startDate = stringToDate(request.startDate)
    const endDate = stringToDate(request.endDate)
    const dayIndexes = getDistanceInDays(startDate, endDate)
    return request.employees
        .sort((a, b) => a.index - b.index)
        .map(assignment => {
            const employeeId = assignment.employeeId
            let highLightIndexes: number[] = []
            if (highlightInfo.originCell && highlightInfo.originCell.employeeId === employeeId) {
                highLightIndexes = getIndexesBetweenCells(highlightInfo.originCell, highlightInfo.lastCell!)
            }
            const relatedPattern = shiftPatterns.get(employeeId)
            const cells = dayIndexes
                .map(dayOffset => {
                    const cellDate = new Date(startDate)
                    cellDate.setDate(startDate.getDate() + dayOffset)
                    const relatedRequest = shiftRequests.get(dateToString(cellDate) + employeeId)
                    const cellShiftAndColor = getResultingShiftAndColor(
                        results,
                        employeeId,
                        cellDate,
                        dayOffset,
                        relatedRequest,
                        relatedPattern,
                        mode
                    );

                    return {
                        shift: cellShiftAndColor.shift,
                        index: dayOffset,
                        employeeId: employeeId,
                        date: cellDate,
                        isHighlighted: highLightIndexes.find(i => i === dayOffset) !== undefined,
                        requestId: relatedRequest?.id,
                        color: cellShiftAndColor.color
                    } as Cell
                })
            const relatedShiftPerScheduleRequests = shiftPerSchedule.filter(r => r.owner === employeeId)
            const displayShiftCount = relatedShiftPerScheduleRequests
                .map(r => Math.floor((r.softMin + r.softMax) / 2))
                .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
            const allowedDeviation = Math.floor(relatedShiftPerScheduleRequests
                    .map(r => (r.hardMax - r.softMax))
                    .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
                / relatedShiftPerScheduleRequests.length)

            const assignments = results ? Object.values(results?.assignments[employeeId]!) : []
            const assignedTeam = teamAssignments.find(a => a.owner === employeeId)

            return {
                owner: employeeId,
                rowTitle: createRowTitle(employees.find(e => e.id === employeeId)!, displayShiftCount, allowedDeviation, assignments, assignedTeam),
                cells,
                issues: []
            } as Row
        })
}

function createRowTitle(
    referencedEmployee: EmployeeDTO,
    displayShiftCount: number,
    allowedDeviation: number,
    assignedWorkShifts: WorkShifts[],
    teamAssignment?: TeamAssignmentConstraintDTO
) {
    let title = referencedEmployee.lastName + " " + referencedEmployee.firstName + " (" + displayShiftCount
    const assignedWorkShiftCount = assignedWorkShifts.filter(s => s === WorkShifts.DAY || s === WorkShifts.NIGHT).length
    return (
        <VerticalLayout theme={"spacing"}>
            <HorizontalLayout theme={"spacing"}
                              style={{justifyContent: "space-between", width: "100%", alignItems: "baseline"}}>
                {teamAssignment ?
                    <div style={{
                        width: 20,
                        height: 20,
                        backgroundColor: teamColors[teamAssignment.teamId % 8],
                        alignItems: "center",
                        justifyContent: "center",
                        textAlign: "center",
                        paddingBottom: 5,
                        color: "black"
                    }}>{teamAssignment.teamId}{teamAssignment.isLeader ? "*" : ""}</div> : null
                }
                <p>{title}<sup> ~{allowedDeviation}</sup>)</p>
                <p>{assignedWorkShiftCount}</p>
            </HorizontalLayout>
        </VerticalLayout>

    )
}

function getResultingShiftAndColor(
    results: SolverSolutionDTO | undefined,
    employeeId: string,
    cellDate: Date,
    cellIndex: number,
    shiftRequest: RequestedShiftConstraintDTO | undefined,
    patternRequest: ShiftPatternConstraintDTO | undefined,
    mode: GridDisplayMode
): CellShiftAndColor {
    if (results && mode === GridDisplayMode.RESULT) {
        const assignedShift = results.assignments[employeeId][dateToString(cellDate)] || WorkShifts.ANY
        return {
            shift: assignedShift === WorkShifts.OFF ? WorkShifts.ANY : assignedShift,
            color: undefined
        }
    } else {
        if (shiftRequest) {
            return {
                shift: shiftRequest.requestedShift,
                color: undefined
            }
        } else {
            if (patternRequest && patternRequest.shiftPattern.length > 0) {
                const index = cellIndex + patternRequest.startDayIndex
                return {
                    shift: patternRequest.shiftPattern[index % patternRequest.shiftPattern.length],
                    color: "var(--lumo-contrast-30pct)"
                }
            } else {
                return {shift: WorkShifts.ANY, color: undefined};
            }
        }
    }
}

function getIndexesBetweenCells(
    origin: Cell,
    end: Cell
): number[] {
    const [lowEnd, highEnd] = origin.index < end.index
        ? [origin.index, end.index]
        : [end.index, origin.index]
    const indexes = []
    for (let i = lowEnd; i <= highEnd; i++) {
        indexes.push(i)
    }
    return indexes
}
