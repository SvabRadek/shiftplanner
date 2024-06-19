import { utils, writeFile } from "xlsx"
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/repository/EmployeeRecord";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/api/WorkShifts";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverSolutionDTO";
import { dateToString, stringToDate } from "Frontend/util/utils";

export function exportToExcel(employees: EmployeeRecord[], result: SolverSolutionDTO) {
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
  employeeIds.forEach(employeeId => {
    const row: string[] = []
    const employeeRecord = employees.find(e => e.id.toString() === employeeId)
    const employeesAssignments = result.assignments[employeeId]
    const employeeName = employeeRecord?.lastName + " " + employeeRecord?.firstName
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