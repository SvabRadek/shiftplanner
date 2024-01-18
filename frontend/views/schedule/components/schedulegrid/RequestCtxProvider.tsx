import { createContext, FC, ReactNode, useState } from "react";
import EmployeeRecord from "Frontend/generated/com/cocroachden/planner/employee/EmployeeRecord";
import WorkShifts from "Frontend/generated/com/cocroachden/planner/solver/schedule/WorkShifts";
import SpecificShiftRequestResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/ConstraintRequestService/SpecificShiftRequestResponse";
import { areShiftRequestsDuplicate } from "Frontend/util/utils";
import StupidDate from "Frontend/generated/com/cocroachden/planner/configuration/StupidDate";
import PlannerConfigurationResponse
  from "Frontend/generated/com/cocroachden/planner/configuration/PlannerConfigurationService/PlannerConfigurationResponse";

export type Owner = string
export type Index = number


export type RequestCtxType = {
  request?: PlannerConfigurationResponse
  allEmployees: EmployeeRecord[],
  specificShiftRequests: SpecificShiftRequestResponse[]
  setRequest: (value: PlannerConfigurationResponse) => void
  setEmployees: (value: EmployeeRecord[]) => void
  removeEmployeeFromRequest: (id: string) => void
  addEmployeeToRequest: (id: string) => void
  addSpecificShiftRequest: (shift: WorkShifts, owner: Owner, date: StupidDate) => void
}

export const RequestCtx = createContext<RequestCtxType>({
  setRequest: value => {
  },
  allEmployees: [],
  specificShiftRequests: [],
  setEmployees: value => {
  },
  removeEmployeeFromRequest: id => {
  },
  addEmployeeToRequest: id => {
  },
  addSpecificShiftRequest: (shift, owner) => {
  }
});
export const RequestCtxProvider: FC<{ children?: ReactNode | undefined }> = props => {

  const [request, setRequest] = useState<PlannerConfigurationResponse | undefined>()
  const [allEmployees, setAllEmployees] = useState<EmployeeRecord[]>([])
  const [specificShiftRequests, setSpecificShiftRequests] = useState<SpecificShiftRequestResponse[]>([])

  function removeEmployeeFromRequest(id: string) {
    setRequest(previous => {
      if (!previous) return undefined
      return {
        ...previous,
        workers: previous.workers.filter(w => w.workerId !== id)
      }
    })
  }

  function addEmployeeToRequest(id: string) {
    setRequest(previous => {
      if (!previous) return undefined
      return {
        ...previous,
        workers: [...previous.workers, allEmployees.find(w => w.workerId === id)!]
      }
    })
  }

  function addSpecificShiftRequest(shift: WorkShifts, owner: Owner, date: StupidDate) {
    const newRequest = { requestedShift: shift, owner: owner, date: date, type: "SpecificShiftRequest" }
    setSpecificShiftRequests(prevState => [
      ...prevState.filter(r => !areShiftRequestsDuplicate(r, newRequest)),
      newRequest
    ])
  }

  return (
    <RequestCtx.Provider value={{
      request,
      allEmployees,
      specificShiftRequests,
      setRequest: request => setRequest(() => request),
      setEmployees: employees => setAllEmployees(() => employees),
      removeEmployeeFromRequest,
      addEmployeeToRequest,
      addSpecificShiftRequest
    }}>
      {props.children}
    </RequestCtx.Provider>
  );
};
