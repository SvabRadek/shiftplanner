package com.cocroachden.planner;

import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationId;
import com.cocroachden.planner.solverconfiguration.EmployeeAssignmentRecord;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRepository;
import com.cocroachden.planner.solverconfiguration.SolverConfigurationRecord;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class StartupService {

    @EventListener
    @Transactional
    public void on(ApplicationStartedEvent event) {
        var context = event.getApplicationContext();
        var configRepo = context.getBean(SolverConfigurationRepository.class);
        var assignmentRepo = context.getBean(EmployeeAssignmentRepository.class);
        var employeeRepo = context.getBean(EmployeeRepository.class);
        if (isThereAnyConfig(configRepo)) {
            return;
        }
        var configRecord = new SolverConfigurationRecord();
        configRecord.setId(SolverConfigurationId.random());
        configRecord.setStartDate(LocalDate.now());
        configRecord.setEndDate(LocalDate.now().plusDays(30));
        configRecord.setName("Příklad konfigurace");
        var savedConfiguration = configRepo.save(configRecord);
        var employeeRecords = DefaultSolverConfiguration.employees();
        var index = new AtomicInteger(0);
        var assignments = employeeRecords.stream()
                .map(employeeRepo::save)
                .map(e -> {
                    var assignment = new EmployeeAssignmentRecord(savedConfiguration, e, index.getAndIncrement(), 1);
                    e.getAssignments().add(assignment);
                    return assignment;
                }).toList();
        savedConfiguration.setEmployeeAssignments(assignments);
        savedConfiguration.setConstraintRequestRecords(
                DefaultSolverConfiguration.constraintRequests().stream()
                        .peek(c -> c.setParent(savedConfiguration))
                        .toList()
        );
        configRepo.save(savedConfiguration);
        assignmentRepo.saveAll(assignments);
        employeeRepo.saveAll(employeeRecords);
    }

    private static boolean isThereAnyConfig(SolverConfigurationRepository configRepo) {
        return configRepo.findAll().iterator().hasNext();
    }

}
