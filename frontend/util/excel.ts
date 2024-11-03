import {read, utils, WorkSheet, writeFile} from "xlsx"
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";
import {dateToString, stringToDate} from "Frontend/util/utils";
import EmployeeAssignmentDTO
  from "Frontend/generated/com/cocroachden/planner/solverconfiguration/EmployeeAssignmentDTO";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/SolverSolutionDTO";
import {workShiftBindings} from "Frontend/views/schedule/WorkShiftBindigs";

export function exportToExcel(filename: string, assignments: EmployeeAssignmentDTO[], employees: EmployeeDTO[], solution: SolverSolutionDTO) {
  const rows: string[][] = []
  const employeeIds = Object.keys(solution.assignments)
  const dates = Object.keys(solution.assignments[employeeIds[0]])
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
      const employeesAssignments = solution.assignments[assignment.employeeId]
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

type ImportedRequestedShifts = { shift: WorkShifts, offset: number, row: number }
type ImportedShiftCountRequest = { count: number, deviation: number, row: number }
type ImportedEmployee = Omit<EmployeeDTO, "id"> & { row: number }
export type ImportResult = { employees: ImportedEmployee[], requestedShifts: ImportedRequestedShifts[], requestedShiftCount: ImportedShiftCountRequest[]}

export function importFromExcel(binary: any): ImportResult {
  const importedEmployees: ImportedEmployee[] = []
  const importedAssignments: ImportedRequestedShifts[] = []
  const importedShiftCounts: ImportedShiftCountRequest[] = []
  try {
    const workbook = read(binary, { type: "binary" });
    const firstSheetName = workbook.SheetNames[0];
    const worksheet = workbook.Sheets[firstSheetName];
    const range = utils.decode_range(worksheet["!ref"]!);
    for (let row = range.s.r; row <= range.e.r; row++) {
      const name = getCellValue(row, range.s.c + 1, worksheet)
      if (!doesContainName(name)) continue
      const splitName = name?.split(" ")
      importedEmployees.push({ firstName: splitName![0], lastName: splitName![1], row })
      const optimalShiftCount = Number.parseInt(getCellValue(row, range.s.c, worksheet) || "14");
      importedShiftCounts.push({ count: optimalShiftCount, deviation: calculateDeviation(optimalShiftCount), row })
      for (let col = range.s.c + 1; col <= range.e.c; col++) {
        const assignmentSymbol = getCellValue(row, col, worksheet)
        importedAssignments.push({ shift: findWorShift(assignmentSymbol), offset: col, row })
      }
    }
    return {
      employees: importedEmployees,
      requestedShifts: importedAssignments.filter(a => a.shift != WorkShifts.ANY),
      requestedShiftCount: importedShiftCounts
    }
  } catch (error) {
    console.error("Error reading Excel file:", error);
    throw new Error("Error reading Excel file");
  }
}

function doesContainName(value: string | undefined) {
  if (!value) return false
  return value.split(" ").length > 1
}

function getCellValue(row: number, col: number, sheet: WorkSheet): string | undefined {
  const cell = sheet[utils.encode_cell({ r: row, c: col })]
  return cell ? cell.v : undefined
}

function findWorShift(symbol: string | undefined): WorkShifts {
  if (!symbol) return WorkShifts.ANY
  const binding = Object.values(workShiftBindings).find(b => b.symbol.toLowerCase() === symbol.toLowerCase())
  return binding ? binding.shift : WorkShifts.ANY
}

function calculateDeviation(shiftCount: number): number {
  if (shiftCount < 10) {
    return 0
  }
  if (shiftCount < 14) {
    return 1
  }
  return 2
}