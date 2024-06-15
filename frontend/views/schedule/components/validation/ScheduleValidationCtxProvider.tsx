import { createContext, ReactNode, useMemo, useState } from "react";
import DayValidationIssue from "Frontend/generated/com/cocroachden/planner/constraint/validations/day/DayValidationIssue";
import ConstraintRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintRequestDTO";
import { ConstraintValidationEndpoint } from "Frontend/generated/endpoints";
import IssueSeverity from "Frontend/generated/com/cocroachden/planner/constraint/validations/IssueSeverity";
import SolverConfigurationDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverConfigurationDTO";
import EmployeeValidationIssue
  from "Frontend/generated/com/cocroachden/planner/constraint/validations/employee/EmployeeValidationIssue";

type ScheduleValidationCtxType = {
  dayIssues: DayValidationIssue[]
  employeeIssues: EmployeeValidationIssue[]
  dayIssueMap: Map<string, DayValidationIssue[]>
  employeeIssueMap: Map<number, EmployeeValidationIssue[]>
  validate: (config: SolverConfigurationDTO, constraints: ConstraintRequestDTO[]) => void
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
  dayIssues: DayValidationIssue[],
  employeeIssues: EmployeeValidationIssue[]
}

export function ScheduleValidationCtxProvider({ children }: { children: ReactNode }) {

  const [issues, setIssues] = useState<IssueContainer>({
    dayIssues: [],
    employeeIssues: []
  });

  const dayIssueMap = useMemo(() => {
    const map = new Map<string, DayValidationIssue[]>
    issues.dayIssues.forEach(i => {
      const existing = map.get(i.localDate)
      map.set(i.localDate, existing ? [...existing, i] : [i])
    })
    return map
  }, [issues.dayIssues])

  const employeeIssueMap = useMemo(() => {
    const map = new Map<number, EmployeeValidationIssue[]>
    issues.employeeIssues.forEach(i => {
      const existing = map.get(i.employeeId.id)
      map.set(i.employeeId.id, existing ? [...existing, i] : [i])
    })
    return map
  }, [issues.employeeIssues])

  function handleValidation(config: SolverConfigurationDTO, constraints: ConstraintRequestDTO[]) {

    ConstraintValidationEndpoint.validateDays({ ...config, constraints }).then(dayIssues => {
      ConstraintValidationEndpoint.validateEmployees({ ...config, constraints }).then(employeeIssues => {
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
