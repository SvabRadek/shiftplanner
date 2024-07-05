import { utils, writeFile } from "xlsx"
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverSolutionDTO";
import { dateToString, stringToDate } from "Frontend/util/utils";
import AssignedEmployeeDTO from "Frontend/generated/com/cocroachden/planner/solver/api/AssignedEmployeeDTO";

export function exportToExcel(assignments: AssignedEmployeeDTO[], result: SolverSolutionDTO) {
  const rows: string[][] = []
  const employeeIds = Object.keys(result.assignments)
  const dates = Object.keys(result.assignments[employeeIds[0]])
    .map(date => stringToDate(date))
    .sort((a, b) => a.getTime() - b.getTime())
  const firstRow = (): string[] => {
    const row: string[] = []
    row.push("Jmeno")
    dates.forEach(date => row.push(date.getDate().toString()))
    return row
  }
  rows.push(firstRow())
  employeeIds
    .map(id => {
      const assignment = assignments.find(a => a.employee.id.toString() === id)
      if (!assignment) {
        throw new Error("EmployeeId in received results was not found in original assignments. This should never happen!")
      }
      return assignment
    }).sort((a, b) => a.index - b.index)
    .forEach(assignment => {
      const row: string[] = [];
      const employeesAssignments = result.assignments[assignment.employee.id]
      const employeeName = assignment.employee.lastName + " " + assignment.employee.firstName
      row.push(employeeName)
      dates.forEach(date => {
        const assignedShift = employeesAssignments[dateToString(date)]
        row.push(assignedShift === WorkShifts.OFF ? "" : assignedShift.charAt(0))
      })
      rows.push(row)
    })
  const sheet = utils.aoa_to_sheet(rows)
  const wb = utils.book_new()
  utils.book_append_sheet(wb, sheet, "rozvrh")
  writeFile(wb, "Rozvrh.xlsx", { compression: true })
}