import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/WorkShifts";
import RequestedShiftConstraintDTO
  from "Frontend/generated/com/cocroachden/planner/constraint/RequestedShiftConstraintDTO";

export enum CRUDActions {
  CREATE,
  READ,
  UPDATE,
  DELETE
}

export type CrudAction<T> = {
  type: CRUDActions,
  payload: T
}

export function dateToString(date: Date): string {
  return [
    date.getFullYear(),
    doubleDigit(date.getMonth() + 1),
    doubleDigit(date.getDate())
  ].join("-")
}

export function stringToDate(date: string): Date {
  const dateArray = date.split("-")
  return new Date(
    Number.parseInt(dateArray[0]),
    Number.parseInt(dateArray[1]) - 1,
    Number.parseInt(dateArray[2])
  )
}

export function getDistanceInDaysNumeric(startDate: Date, endDate: Date): number {
  const diffTimeInMillis = Math.abs(endDate.valueOf() - startDate.valueOf());
  return diffTimeInMillis / (1000 * 60 * 60 * 24)
}

type IdentifiableSpecificShiftRequestDTO = Pick<RequestedShiftConstraintDTO, "owner" | "date">

export function areShiftRequestsSame(r1: IdentifiableSpecificShiftRequestDTO, r2: IdentifiableSpecificShiftRequestDTO): boolean {
  return [r1.owner, r1.date].join() === [r2.owner, r2.date].join()
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

export function arePatternsSame(shifts1: WorkShifts[], shift2: WorkShifts[]): boolean {
  if (shifts1.length !== shift2.length) return false
  for (let i = 0; i < shifts1.length; i++) {
    if (shifts1[i] !== shift2[i]) return false
  }
  return true
}

