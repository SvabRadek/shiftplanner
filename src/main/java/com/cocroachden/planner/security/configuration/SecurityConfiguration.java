package com.cocroachden.planner.security.configuration;

import com.cocroachden.planner.user.query.RegisteredUserQuery;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfiguration extends VaadinWebSecurity {

    private final RegisteredUserQuery registeredUserQuery;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/")).permitAll());
        super.configure(http);
        http.userDetailsService(registeredUserQuery);
        setLoginView(http, "/login");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
