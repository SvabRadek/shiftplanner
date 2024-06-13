import { createContext, ReactNode, useMemo, useState } from "react";
import DayValidationIssue from "Frontend/generated/com/cocroachden/planner/constraint/validations/day/DayValidationIssue";
import ConstraintRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintRequestDTO";
import { ConstraintValidationEndpoint } from "Frontend/generated/endpoints";
import { stupidDateToString } from "Frontend/util/utils";
import IssueSeverity from "Frontend/generated/com/cocroachden/planner/constraint/validations/IssueSeverity";
import SolverConfigurationDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverConfigurationDTO";
import EmployeeValidationIssue
  from "Frontend/generated/com/cocroachden/planner/constraint/validations/employee/EmployeeValidationIssue";

type ScheduleValidationCtxType = {
  dayIssues: DayValidationIssue[]
  workerIssues: EmployeeValidationIssue[]
  dayIssueMap: Map<string, DayValidationIssue[]>
  workerIssueMap: Map<number, EmployeeValidationIssue[]>
  validate: (config: SolverConfigurationDTO, constraints: ConstraintRequestDTO[]) => void
  clear: () => void
  containsIssues: boolean
  getSeverityOfIssues: (issues: { severity: IssueSeverity }[]) => IssueSeverity
}

export const ValidationContext = createContext<ScheduleValidationCtxType>({
  dayIssues: [],
  workerIssues: [],
  dayIssueMap: new Map,
  workerIssueMap: new Map,
  validate: () => {
  },
  clear: () => {
  },
  containsIssues: false,
  getSeverityOfIssues: () => IssueSeverity.UNKNOWN

});

type IssueContainer = {
  dayIssues: DayValidationIssue[],
  workerIssues: EmployeeValidationIssue[]
}

export function ScheduleValidationCtxProvider({ children }: { children: ReactNode }) {

  const [issues, setIssues] = useState<IssueContainer>({
    dayIssues: [],
    workerIssues: []
  });

  const dayIssueMap = useMemo(() => {
    const map = new Map<string, DayValidationIssue[]>
    issues.dayIssues.forEach(i => {
      const existing = map.get(stupidDateToString(i.localDate))
      map.set(stupidDateToString(i.localDate), existing ? [...existing, i] : [i])
    })
    return map
  }, [issues.dayIssues])

  const workerIssueMap = useMemo(() => {
    const map = new Map<number, EmployeeValidationIssue[]>
    issues.workerIssues.forEach(i => {
      const existing = map.get(i.employeeId.id)
      map.set(i.employeeId.id, existing ? [...existing, i] : [i])
    })
    return map
  }, [issues.workerIssues])

  function handleValidation(config: SolverConfigurationDTO, constraints: ConstraintRequestDTO[]) {

    ConstraintValidationEndpoint.validateDays({ ...config, constraints }).then(dayIssues => {
      ConstraintValidationEndpoint.validateWorkers({ ...config, constraints }).then(workerIssues => {
        setIssues({
          workerIssues: workerIssues,
          dayIssues: dayIssues
        })
      })
    })

  }

  function handleClearing() {
    setIssues({
      workerIssues: [],
      dayIssues: []
    })
  }

  function containsIssues() {
    return issues.workerIssues.length + issues.dayIssues.length > 0
  }

  function getSeverityOfIssues(issues: { severity: IssueSeverity }[]): IssueSeverity {
    if (issues.some(i => i.severity === IssueSeverity.ERROR)) return IssueSeverity.ERROR
    if (issues.some(i => i.severity === IssueSeverity.WARNING)) return IssueSeverity.WARNING
    return IssueSeverity.OK
  }

  return (
    <ValidationContext.Provider value={{
      workerIssues: issues.workerIssues,
      dayIssues: issues.dayIssues,
      dayIssueMap,
      workerIssueMap,
      validate: handleValidation,
      clear: handleClearing,
      containsIssues: containsIssues(),
      getSeverityOfIssues: issues => getSeverityOfIssues(issues),
    }}>
      {children}
    </ValidationContext.Provider>
  );
}
