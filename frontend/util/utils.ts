import StupidDate from "Frontend/generated/com/cocroachden/planner/lib/StupidDate";
import SpecificShiftRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/SpecificShiftRequestDTO";
import ShiftsPerScheduleRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftsPerScheduleRequestDTO";
import EmployeesPerShiftRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/EmployeesPerShiftRequestDTO";
import ShiftFollowupRestrictionRequestDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/ShiftFollowupRestrictionRequestDTO";

export function dateToStupidDate(date: Date): StupidDate {
  return { day: date.getDate(), month: date.getMonth() + 1, year: date.getFullYear() }
}

export function stupidDateToString(date: StupidDate): string {
  return [
    doubleDigit(date.day),
    doubleDigit(date.month),
    date.year.toString()
  ].join("/")
}

export function stupidDateToLocaleDate(date: StupidDate): string {
  return [
    date.year,
    doubleDigit(date.month),
    doubleDigit(date.day)
  ].join("-")
}

export function dateToLocaleDate(date: Date): string {
  return [
    date.getFullYear(),
    doubleDigit(date.getUTCMonth() + 1),
    doubleDigit(date.getUTCDate())
  ].join("-")
}

export function localeDateToStupidDate(date: string): StupidDate {
  console.log(date)
  const dateArray = date.split("-")
  return {
    year: Number.parseInt(dateArray[0]),
    month: Number.parseInt(dateArray[1]),
    day: Number.parseInt(dateArray[2]),
  }
}

export function stupidDateToDate(date: StupidDate): Date {
  return new Date(date.year,date.month -1, date.day)
}

export function dateToString(date: Date): string {
  return stupidDateToString(dateToStupidDate(date))
}

export function getDistanceInDaysNumeric(startDate: Date, endDate: Date): number {
  const diffTimeInMillis = Math.abs(endDate.valueOf() - startDate.valueOf());
  return diffTimeInMillis / (1000 * 60 * 60 * 24)
}

export function areShiftRequestsSame(r1: SpecificShiftRequestDTO, r2: SpecificShiftRequestDTO): boolean {
  return [r1.owner, stupidDateToString(r1.date)].join() === [r2.owner, stupidDateToString(r2.date)].join()
}

type IdentifiableShiftsPerScheduleRequestDTO = Pick<ShiftsPerScheduleRequestDTO, "owner" | "targetShift">
export function areShiftPerScheduleSame(
  r1: IdentifiableShiftsPerScheduleRequestDTO,
  r2: IdentifiableShiftsPerScheduleRequestDTO
): boolean {
  return (r1.owner.workerId + r1.targetShift.toString()) === (r2.owner.workerId + r2.targetShift.toString())
}

type IdentifiableEmployeesPerShiftRequestDTO = Pick<EmployeesPerShiftRequestDTO, "targetShift">

export function areEmployeesPerShiftSame(
  r1: IdentifiableEmployeesPerShiftRequestDTO,
  r2: IdentifiableEmployeesPerShiftRequestDTO
) {
  return r1.targetShift.toString() === r2.targetShift.toString()
}

type IdentifiableShiftFollowupRestrictionRequestDTO = Pick<ShiftFollowupRestrictionRequestDTO, "firstShift" | "forbiddenFollowup">

export function areShiftFollowupRestrictionsSame(
  r1: IdentifiableShiftFollowupRestrictionRequestDTO,
  r2: IdentifiableShiftFollowupRestrictionRequestDTO
) {
  return r1.firstShift.toString() + r1.forbiddenFollowup.toString() === r2.firstShift.toString() + r2.forbiddenFollowup.toString()
}

export function generateUUID() { // Public Domain/MIT
  var d = new Date().getTime();//Timestamp
  var d2 = ((typeof performance !== 'undefined') && performance.now && (performance.now()*1000)) || 0;//Time in microseconds since page-load or 0 if unsupported
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    var r = Math.random() * 16;//random number between 0 and 16
    if(d > 0){//Use timestamp until depleted
      r = (d + r)%16 | 0;
      d = Math.floor(d/16);
    } else {//Use microseconds since page-load if supported
      r = (d2 + r)%16 | 0;
      d2 = Math.floor(d2/16);
    }
    return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
  });
}

function doubleDigit(num: number): string {
  return num.toLocaleString("en-US", {
    minimumIntegerDigits: 2,
    useGrouping: false
  })
}
