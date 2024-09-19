import {utils, writeFile} from "xlsx"
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";
import {dateToString, stringToDate} from "Frontend/util/utils";
import EmployeeAssignmentDTO
  from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/SolverSolutionDTO";

export function exportToExcel(filename: string, assignments: EmployeeAssignmentDTO[], employees: EmployeeDTO[], result: SolverSolutionDTO) {
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
      const assignment = assignments.find(a => a.employeeId.toString() === id)
      if (!assignment) {
        throw new Error("EmployeeId in received results was not found in original assignments. This should never happen!")
      }
      return assignment
    }).sort((a, b) => a.index - b.index)
    .forEach(assignment => {
      const row: string[] = [];
      const employee = employees.find(e => e.id === assignment.employeeId)!
      const employeesAssignments = result.assignments[assignment.employeeId]
      const employeeName = employee.lastName + " " + employee.firstName
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
  writeFile(wb, filename + ".xlsx", { compression: true })
}