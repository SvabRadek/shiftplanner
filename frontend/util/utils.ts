import StupidDate from "Frontend/generated/com/cocroachden/planner/lib/StupidDate";
import SpecificShiftRequestResponse
  from "Frontend/generated/com/cocroachden/planner/constraint/service/ConstraintRequestService/SpecificShiftRequestResponse";

export function dateToStupidDate(date: Date): StupidDate {
  return { day: date.getDate(), month: date.getMonth() + 1, year: date.getFullYear() }
}

export function stupidDateToString(date: StupidDate): string {
  return [date.day, date.month, date.year].join("/")
}

export function stupidDateToDate(date: StupidDate): Date {
  return new Date(date.year,date.month - 1, date.day)
}

export function dateToString(date: Date): string {
  return stupidDateToString(dateToStupidDate(date))
}

export function getDistanceInDaysNumeric(startDate: Date, endDate: Date): number {
  const diffTimeInMillis = Math.abs(endDate.valueOf() - startDate.valueOf());
  return diffTimeInMillis / (1000 * 60 * 60 * 24)
}

export function areShiftRequestsDuplicate(r1: SpecificShiftRequestResponse, r2: SpecificShiftRequestResponse): boolean {
  return [r1.owner, stupidDateToString(r1.date)].join() === [r2.owner, stupidDateToDate(r2.date)].join()
}
