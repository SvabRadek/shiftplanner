package com.cocroachden.planner.fixtures.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "application.fixtures")
@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class FixtureModuleConfiguration {
    public Boolean enabled = true;
    public String[] fixturesToApply = new String[]{};
}
