// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs.api;

import java.util.Properties;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.talend.daikon.spring.audit.logs.config.AuditLogAutoConfiguration;
import org.talend.logging.audit.impl.AuditLoggerBase;

import io.micrometer.core.instrument.Counter;

@TestConfiguration
public class AppTestConfig extends AuditLogAutoConfiguration {

    @Bean
    @Override
    public AuditLoggerBase auditLoggerBase(Properties kafkaProperties) {
        return Mockito.mock(AuditLoggerBase.class);
    }

    @Bean
    public TenancyContextWebFilter tenancyContextWebFilter() {
        return new TenancyContextWebFilter();
    }

    @Bean
    @Primary
    public Counter auditLogsGeneratedCounter() {
        return Mockito.mock(Counter.class);
    }

    @Bean
    public SecurityWebFilterChain publicApiSecurityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable) // we can disable CSRF because this service only use JWT token
                                                               // through Authorization header
                .authorizeExchange(a -> a.anyExchange().permitAll()).build();
    }
}
