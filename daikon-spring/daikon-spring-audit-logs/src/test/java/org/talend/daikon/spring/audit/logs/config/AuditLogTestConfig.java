// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs.config;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.talend.daikon.spring.audit.logs.api.AuditLogTestApp;
import org.talend.daikon.spring.audit.logs.api.AuditUserProvider;

@Configuration
@EnableWebSecurity
public class AuditLogTestConfig {

    @Bean("securityFilterChain.auditLogTestConfig")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED)));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder().username(AuditLogTestApp.USERNAME).password(RandomStringUtils.random(10)).roles()
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public AuditUserProvider auditUserProvider() {
        return new AuditUserProvider() {

            @Override
            public String getUsername() {
                return Optional.ofNullable(SecurityContextHolder.getContext()).map(SecurityContext::getAuthentication)
                        .map(Authentication::getPrincipal).filter(principal -> principal instanceof UserDetails)
                        .map(UserDetails.class::cast).map(UserDetails::getUsername).orElse(null);
            }

            @Override
            public String getUserId() {
                return StringUtils.isBlank(getUsername()) ? null : AuditLogTestApp.USER_ID;
            }

            @Override
            public String getUserEmail() {
                return StringUtils.isBlank(getUsername()) ? null : AuditLogTestApp.USER_EMAIL;
            }

            @Override
            public String getAccountId() {
                return StringUtils.isBlank(getUsername()) ? null : AuditLogTestApp.ACCOUNT_ID;
            }
        };
    }
}
