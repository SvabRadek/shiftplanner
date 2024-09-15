package com.cocroachden.planner;

import com.cocroachden.planner.employee.repository.EmployeeRepository;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRecord;
import com.cocroachden.planner.solverconfiguration.repository.EmployeeAssignmentRepository;
import com.cocroachden.planner.solverconfiguration.repository.SolverConfigurationRecord;
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
        var configRecord = new SolverConfigurationRecord()
                .setId("9b314fa8-b459-427e-b3d8-2db53dc48d73")
                .setStartDate(LocalDate.now())
                .setEndDate(LocalDate.now().plusDays(30))
                .setName("Příklad konfigurace");
        var savedConfiguration = configRepo.save(configRecord);
        var employeeRecords = DefaultSolverConfiguration.employees();
        var index = new AtomicInteger(0);
        var assignments = employeeRecords.stream()
                .map(employeeRepo::save)
                .map(e -> new EmployeeAssignmentRecord()
                        .setIndex(index.getAndIncrement())
                        .setWeight(1)
                        .setConfiguration(savedConfiguration)
                        .setEmployee(e)
                ).toList();
        DefaultSolverConfiguration.constraintRequests().forEach(c -> c.setParent(savedConfiguration));
        configRepo.save(savedConfiguration);
        assignmentRepo.saveAll(assignments);
        employeeRepo.saveAll(employeeRecords);
    }

    private static boolean isThereAnyConfig(SolverConfigurationRepository configRepo) {
        return configRepo.findAll().iterator().hasNext();
    }

}
