package com.cocroachden.planner.solver.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.solver")
@Getter
@Setter
public class SolverModuleConfiguration {
    private Integer defaultSolverTimeLimitInSec = 60;
}
