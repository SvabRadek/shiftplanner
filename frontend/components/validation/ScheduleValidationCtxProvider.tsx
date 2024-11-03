import {createContext, ReactNode, useMemo, useState} from "react";
import DayValidationIssueDTO
  from "Frontend/generated/com/cocroachden/planner/solverconfiguration/validations/day/DayValidationIssueDTO";
import EmployeeValidationIssueDTO
  from "Frontend/generated/com/cocroachden/planner/solverconfiguration/validations/employee/EmployeeValidationIssueDTO";
import SolverConfigurationDTO
  from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import IssueSeverity from "Frontend/generated/com/cocroachden/planner/solverconfiguration/validations/IssueSeverity";
import {SolverConfigurationValidationEndpoint} from "Frontend/generated/endpoints";

type ScheduleValidationCtxType = {
  dayIssues: DayValidationIssueDTO[]
  employeeIssues: EmployeeValidationIssueDTO[]
  dayIssueMap: Map<string, DayValidationIssueDTO[]>
  employeeIssueMap: Map<string, EmployeeValidationIssueDTO[]>
  validate: (config: SolverConfigurationDTO) => void
  clear: () => void
  containsIssues: boolean
  getSeverityOfIssues: (issues: { severity: IssueSeverity }[]) => IssueSeverity
}

export const ValidationContext = createContext<ScheduleValidationCtxType>({
  dayIssues: [],
  employeeIssues: [],
  dayIssueMap: new Map,
  employeeIssueMap: new Map,
  validate: () => {
  },
  clear: () => {
  },
  containsIssues: false,
  getSeverityOfIssues: () => IssueSeverity.UNKNOWN

});

type IssueContainer = {
  dayIssues: DayValidationIssueDTO[],
  employeeIssues: EmployeeValidationIssueDTO[]
}

export function ScheduleValidationCtxProvider({ children }: { children: ReactNode }) {

  const [issues, setIssues] = useState<IssueContainer>({
    dayIssues: [],
    employeeIssues: []
  });

  const dayIssueMap = useMemo(() => {
    const map = new Map<string, DayValidationIssueDTO[]>
    issues.dayIssues.forEach(i => {
      const existing = map.get(i.localDate)
      map.set(i.localDate, existing ? [...existing, i] : [i])
    })
    return map
  }, [issues.dayIssues])

  const employeeIssueMap = useMemo(() => {
    const map = new Map<string, EmployeeValidationIssueDTO[]>
    issues.employeeIssues.forEach(i => {
      const existing = map.get(i.employeeId)
      map.set(i.employeeId, existing ? [...existing, i] : [i])
    })
    return map
  }, [issues.employeeIssues])

  function handleValidation(config: SolverConfigurationDTO) {
    SolverConfigurationValidationEndpoint.validateDays(config).then(dayIssues => {
      SolverConfigurationValidationEndpoint.validateEmployees(config).then(employeeIssues => {
        setIssues({
          employeeIssues: employeeIssues,
          dayIssues: dayIssues
        })
      })
    })

  }

  function handleClearing() {
    setIssues({
      employeeIssues: [],
      dayIssues: []
    })
  }

  function containsIssues() {
    return issues.employeeIssues.length + issues.dayIssues.length > 0
  }

  function getSeverityOfIssues(issues: { severity: IssueSeverity }[]): IssueSeverity {
    if (issues.some(i => i.severity === IssueSeverity.ERROR)) return IssueSeverity.ERROR
    if (issues.some(i => i.severity === IssueSeverity.WARNING)) return IssueSeverity.WARNING
    return IssueSeverity.OK
  }

  return (
    <ValidationContext.Provider value={{
      employeeIssues: issues.employeeIssues,
      dayIssues: issues.dayIssues,
      dayIssueMap,
      employeeIssueMap,
      validate: handleValidation,
      clear: handleClearing,
      containsIssues: containsIssues(),
      getSeverityOfIssues: issues => getSeverityOfIssues(issues),
    }}>
      {children}
    </ValidationContext.Provider>
  );
}
