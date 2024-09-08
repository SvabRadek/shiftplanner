import {useMemo, useState} from "react";
import {CrudAction, dateToString, stringToDate} from "Frontend/util/utils";
import {Row, ScheduleGrid} from "Frontend/views/schedule/components/schedulegrid/ScheduleGrid";
import {Cell} from "Frontend/views/schedule/components/schedulegrid/GridCell";
import ShiftsPerScheduleRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftsPerScheduleRequestDTO";
import ShiftPatternRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ShiftPatternRequestDTO";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import SolverConfigurationDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverConfigurationDTO";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverSolutionDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";
import ConstraintType from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintType";
import EmployeeShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/EmployeeShiftRequestDTO";
import EmployeeId from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeId";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/api/EmployeeDTO";
import AssignedEmployeeDTO from "Frontend/generated/com/cocroachden/planner/solver/api/AssignedEmployeeDTO";
import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import TeamAssignmentRequestDTO
    from "Frontend/generated/com/cocroachden/planner/constraint/api/TeamAssignmentRequestDTO";

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
    shiftPatterns: ShiftPatternRequestDTO[]
    shiftRequests: EmployeeShiftRequestDTO[]
    teamAssignments: TeamAssignmentRequestDTO[]
    shiftPerScheduleRequests: ShiftsPerScheduleRequestDTO[]
    onAssignmentAction: (action: CrudAction<AssignedEmployeeDTO>) => void
    onShiftRequestsChanged?: (changedRequests: Omit<EmployeeShiftRequestDTO, "id">[]) => void
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
                type: ConstraintType.EMPLOYEE_SHIFT_REQUEST,
                owner: {id: updatedCell.employeeId},
                date: dateToString(updatedCell.date),
                requestedShift: updatedCell.shift
            }
        ])
    }

    function handleGroupShiftChange(originCell: Cell, endCell: Cell) {
        const originDate = originCell.index < endCell.index ? originCell.date : endCell.date
        const lowEnd = originCell.index < endCell.index ? originCell.index : endCell.index
        const highEnd = originCell.index > endCell.index ? originCell.index : endCell.index
        const requests: Omit<EmployeeShiftRequestDTO, "id">[] = []
        for (let i = lowEnd; i <= highEnd; i++) {
            const index = i - lowEnd
            const requestDate = new Date(originDate)
            requestDate.setDate(requestDate.getDate() + index)
            requests.push({
                    type: ConstraintType.EMPLOYEE_SHIFT_REQUEST,
                    owner: {id: originCell.employeeId},
                    requestedShift: originCell.shift,
                    date: dateToString(requestDate)
                } as Omit<EmployeeShiftRequestDTO, "id">
            )
        }
        props.onShiftRequestsChanged?.(requests)
    }

    const shiftRequestMap = useMemo(() => {
        const map = new Map<string, EmployeeShiftRequestDTO>()
        props.shiftRequests.forEach(r => map.set(r.date + r.owner.id, r))
        return map
    }, [props.shiftRequests])

    const shiftPatternMap = useMemo(() => {
        const map = new Map<PlainEmployeeId, ShiftPatternRequestDTO>()
        props.shiftPatterns.forEach(r => map.set(r.owner?.id || -1, r))
        return map
    }, [props.shiftPatterns])

    return (
        <ScheduleGrid
            rows={
                createRows(
                    props.request,
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
                payload: props.request.employees.find(e => e.employee.id === a.payload.id)!
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
    shiftRequests: Map<string, EmployeeShiftRequestDTO>,
    shiftPatterns: Map<PlainEmployeeId, ShiftPatternRequestDTO>,
    shiftPerSchedule: ShiftsPerScheduleRequestDTO[],
    teamAssignments: TeamAssignmentRequestDTO[],
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
            const employeeDto = assignment.employee
            let highLightIndexes: number[] = []
            if (highlightInfo.originCell && highlightInfo.originCell.employeeId === employeeDto.id) {
                highLightIndexes = getIndexesBetweenCells(highlightInfo.originCell, highlightInfo.lastCell!)
            }
            const relatedPattern = shiftPatterns.get(employeeDto.id)
            const cells = dayIndexes
                .map(dayOffset => {
                    const cellDate = new Date(startDate)
                    cellDate.setDate(startDate.getDate() + dayOffset)
                    const relatedRequest = shiftRequests.get(dateToString(cellDate) + employeeDto.id)
                    const cellShiftAndColor = getResultingShiftAndColor(
                        results,
                        employeeDto,
                        cellDate,
                        dayOffset,
                        relatedRequest,
                        relatedPattern,
                        mode
                    );

                    return {
                        shift: cellShiftAndColor.shift,
                        index: dayOffset,
                        employeeId: employeeDto.id,
                        date: cellDate,
                        isHighlighted: highLightIndexes.find(i => i === dayOffset) !== undefined,
                        requestId: relatedRequest?.id,
                        color: cellShiftAndColor.color
                    } as Cell
                })
            const relatedShiftPerScheduleRequests = shiftPerSchedule.filter(r => r.owner.id === employeeDto.id)
            const displayShiftCount = relatedShiftPerScheduleRequests
                .map(r => Math.floor((r.softMin + r.softMax) / 2))
                .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
            const allowedDeviation = Math.floor(relatedShiftPerScheduleRequests
                    .map(r => (r.hardMax - r.softMax))
                    .reduce((previousValue, currentValue) => previousValue + currentValue, 0)
                / relatedShiftPerScheduleRequests.length)

            const assignments = results ? Object.values(results?.assignments[employeeDto.id]!) : []
            const assignedTeam = teamAssignments.find(a => a.owner?.id === employeeDto.id)

            return {
                owner: employeeDto,
                rowTitle: createRowTitle(employeeDto, displayShiftCount, allowedDeviation, assignments, assignedTeam),
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
    teamAssignment?: TeamAssignmentRequestDTO
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
    employeeId: EmployeeId,
    cellDate: Date,
    cellIndex: number,
    shiftRequest: EmployeeShiftRequestDTO | undefined,
    patternRequest: ShiftPatternRequestDTO | undefined,
    mode: GridDisplayMode
): CellShiftAndColor {
    if (results && mode === GridDisplayMode.RESULT) {
        const assignedShift = results.assignments[employeeId.id][dateToString(cellDate)] || WorkShifts.ANY
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
