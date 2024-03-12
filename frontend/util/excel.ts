import { utils, writeFile } from "xlsx"
import ScheduleResultDTO from "Frontend/generated/com/cocroachden/planner/solver/ScheduleResultDTO";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";

export function exportToExcel(employees: EmployeeRecord[], result: ScheduleResultDTO) {
  const rows = Object.keys(result.assignments).map(owner => {
    const row: Record<string, string> = {}
    const employee = employees.find(e => e.id.toString() === owner)
    row["Jmeno"] = employee?.lastName + " " + employee?.firstName
    Object.keys(result.assignments[owner]).forEach(date => {
      const shift = result.assignments[owner][date]
      row[date] = shift === WorkShifts.OFF ? "" : shift.charAt(0)
    })
    return row
  });
  const sheet = utils.json_to_sheet(rows)
  const wb = utils.book_new()
  utils.book_append_sheet(wb, sheet, "rozvrh")
  writeFile(wb, "Rozvrh.xlsx", { compression: true })
}