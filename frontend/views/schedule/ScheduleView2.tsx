import {VerticalLayout} from "@hilla/react-components/VerticalLayout";
import {Fragment, useEffect, useState} from "react";
import SolverConfigurationDTO
    from "Frontend/generated/com/cocroachden/planner/solverconfiguration/SolverConfigurationDTO";
import {ConfigurationSelectMenu} from "Frontend/components/configurationselect/ConfigurationSelectMenu";
import {ScheduleEditor} from "Frontend/components/solverconfigurationeditor/ScheduleEditor";
import EmployeeDTO from "Frontend/generated/com/cocroachden/planner/employee/EmployeeDTO";
import {
    CustomEmployeeEndpoint,
    SolverConfigurationEndpoint,
    SolverEndpoint,
    TicketEndpoint
} from "Frontend/generated/endpoints";
import {HorizontalLayout} from "@hilla/react-components/HorizontalLayout";
import {PrimaryButton} from "Frontend/components/PrimaryButton";
import {Icon} from "@hilla/react-components/Icon";
import {Card} from "Frontend/components/Card";
import SolverSolutionDTO from "Frontend/generated/com/cocroachden/planner/solver/SolverSolutionDTO";
import {Subscription} from "@hilla/frontend";
import {ResultSubHeaderStrip} from "Frontend/views/schedule/ResultSubHeaderStrip";
import {exportToExcel} from "Frontend/util/excel";
import SolutionNotificationDTO from "Frontend/generated/com/cocroachden/planner/solver/SolutionNotificationDTO";
import SolutionStatus from "Frontend/generated/com/cocroachden/planner/solver/SolutionStatus";
import {ProgressBar} from "@hilla/react-components/ProgressBar";

export type CachedSolutions = {
    currentIndex: number,
    solutions: SolverSolutionDTO[]
}

export function ScheduleView2() {

    const [solverSubscription, setSolverSubscription] = useState<Subscription<SolutionNotificationDTO> | undefined>();
    const [cachedSolutions, setCachedSolutions] = useState<CachedSolutions | undefined>(undefined);
    const [solverConfiguration, setSolverConfiguration] = useState<SolverConfigurationDTO | undefined>();
    const [isConfigurationAlreadySaved, setIsConfigurationAlreadySaved] = useState(false);
    const [employees, setEmployees] = useState<EmployeeDTO[]>([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        if (solverConfiguration) {
            SolverConfigurationEndpoint.exists(solverConfiguration?.id).then(setIsConfigurationAlreadySaved);
        }
    }, [solverConfiguration]);

    async function handleSave() {
        SolverConfigurationEndpoint.save(solverConfiguration!)
            .then(id => SolverConfigurationEndpoint.getConfiguration(id).then(handleConfigSelection))
    }

    async function handleConfigSelection(selected: SolverConfigurationDTO) {
        setIsLoading(true)
        if (solverSubscription) {
            solverSubscription.cancel()
            setSolverSubscription(undefined)
        }
        const ids = selected.employees.map(a => a.employeeId);
        setEmployees(await CustomEmployeeEndpoint.findEmployees(ids))
        setSolverConfiguration(selected)
        setCachedSolutions(undefined)
        setIsLoading(false)
    }

    async function handleStartCalculation() {
        TicketEndpoint.issueTicket().then(ticket => {
            const subscription = SolverEndpoint.solveSavedProblem(solverConfiguration?.id!, ticket!)
                .onNext(notification => {
                    if (notification.solutionStatus === SolutionStatus.STOPPED) {
                        console.log("Stopping calculation");
                        handleStopCalculation()
                        return
                    }
                    SolverEndpoint.getLatestSolution(notification.subscriptionId)
                        .then(solution => {
                            console.log(solution)
                            if (solution.solutionStatus === SolutionStatus.OK) {
                                setCachedSolutions(prevState => {
                                    if (!prevState) {
                                        return {solutions: [solution], currentIndex: 0}
                                    }
                                    const updatedSolutions = [...prevState.solutions, solution];
                                    return {solutions: updatedSolutions, currentIndex: updatedSolutions.length - 1}
                                });
                            } else {
                                handleStopCalculation()
                            }
                        });
                }).onComplete(() => {
                    console.log("completed, stopping calc")
                    handleStopCalculation()
                })
            setSolverSubscription(subscription)
        })
    }

    async function handleStopCalculation() {
        solverSubscription?.cancel()
        setSolverSubscription(undefined)
    }

    return (
        <VerticalLayout
            theme={"spacing padding"}
            style={{width: "100%", height: "100%", justifyContent: "center", alignItems: "center"}}>
            {isLoading &&
                <VerticalLayout theme={"spacing"} style={{width: "100%", justifyContent: "center" }}>
                    <h4>Loading...</h4>
                    <ProgressBar
                        style={{width: "100%"}}
                        indeterminate={true}
                    />
                </VerticalLayout>

            }
            {solverConfiguration
                ? <Fragment>
                    <Card style={{width: "100%"}}>
                        <HorizontalLayout theme={"spacing"} style={{width: "100%"}}>
                            {solverSubscription
                                ? <PrimaryButton onClick={handleStopCalculation}>
                                    <Icon icon={"vaadin:stop"} slot={"prefix"}/>Stop</PrimaryButton>
                                : <PrimaryButton
                                    onClick={handleStartCalculation}
                                    disabled={!isConfigurationAlreadySaved}
                                >
                                    <Icon icon={"vaadin:play"}
                                          slot={"prefix"}/>Vypočítat</PrimaryButton>
                            }
                            <PrimaryButton onClick={handleSave}><Icon
                                icon={"vaadin:check"} slot={"prefix"}/>Uložit</PrimaryButton>
                        </HorizontalLayout>
                    </Card>
                    {solverSubscription || cachedSolutions ?
                        <ResultSubHeaderStrip
                            onExportToExcel={() => exportToExcel("Rozvrh", solverConfiguration?.employees, employees, cachedSolutions?.solutions[cachedSolutions?.currentIndex]!)}
                            cachedSolutions={cachedSolutions}
                            cacheSize={500}
                            onCachedSolutionsChanged={setCachedSolutions}
                            stopped={solverSubscription === undefined}/> : null
                    }
                    {!isLoading && <ScheduleEditor
                        onConfigurationChanged={setSolverConfiguration}
                        employees={employees}
                        configuration={solverConfiguration}
                        onClose={() => setSolverConfiguration(undefined)}
                        solution={cachedSolutions?.solutions[cachedSolutions.currentIndex]}
                        readonly={solverSubscription !== undefined}
                    />}
                </Fragment>
                : <div style={{paddingTop: "25vh"}}>
                    <ConfigurationSelectMenu onConfigSelected={handleConfigSelection}/>
                </div>
            }
        </VerticalLayout>
    );
}
