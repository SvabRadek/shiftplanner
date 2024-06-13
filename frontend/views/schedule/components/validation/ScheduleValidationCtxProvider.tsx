import { createContext, ReactNode, useMemo, useState } from "react";
import DayValidationIssue from "Frontend/generated/com/cocroachden/planner/constraint/validations/day/DayValidationIssue";
import WorkerValidationIssue
  from "Frontend/generated/com/cocroachden/planner/constraint/validations/worker/WorkerValidationIssue";
import ConstraintRequestDTO from "Frontend/generated/com/cocroachden/planner/constraint/api/ConstraintRequestDTO";
import { ConstraintValidationEndpoint } from "Frontend/generated/endpoints";
import { stupidDateToString } from "Frontend/util/utils";
import IssueSeverity from "Frontend/generated/com/cocroachden/planner/constraint/validations/IssueSeverity";
import SolverConfigurationDTO from "Frontend/generated/com/cocroachden/planner/solver/api/SolverConfigurationDTO";

type ScheduleValidationCtxType = {
  dayIssues: DayValidationIssue[]
  workerIssues: WorkerValidationIssue[]
  dayIssueMap: Map<string, DayValidationIssue[]>
  workerIssueMap: Map<number, WorkerValidationIssue[]>
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
  workerIssues: WorkerValidationIssue[]
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
    const map = new Map<number, WorkerValidationIssue[]>
    issues.workerIssues.forEach(i => {
      const existing = map.get(i.workerId.id)
      map.set(i.workerId.id, existing ? [...existing, i] : [i])
    })
    return map
  }, [issues.workerIssues])

  function handleValidation(config: SolverConfigurationDTO, constraints: ConstraintRequestDTO[]) {
    ConstraintValidationEndpoint.validateDays(config, constraints).then(dayIssues => {
      ConstraintValidationEndpoint.validateWorkers(config, constraints).then(workerIssues => {
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
