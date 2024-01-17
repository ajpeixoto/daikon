// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.spring.consumer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.talend.daikon.messages.header.consumer.CorrelationIdSetter;
import org.talend.daikon.messages.header.consumer.SecurityTokenSetter;
import org.talend.daikon.messages.header.consumer.ServiceAccountIdSetter;
import org.talend.daikon.messages.header.consumer.TenantIdSetter;
import org.talend.daikon.messages.header.consumer.UserIdSetter;

@AutoConfiguration
public class DefaultConsumerSettersConfiguration {

    @Bean
    @ConditionalOnMissingBean(CorrelationIdSetter.class)
    public CorrelationIdSetter noopCorrelationIdSetter() {
        return correlationId -> {
            // mocked bean: do nothing
        };
    }

    @Bean
    @ConditionalOnMissingBean(TenantIdSetter.class)
    public TenantIdSetter noopTenantIdSetter() {
        return tenantId -> {
            // mocked bean: do nothing
        };
    }

    @Bean
    @ConditionalOnMissingBean(UserIdSetter.class)
    public UserIdSetter noopUserIdSetter() {
        return userId -> {
            // mocked bean: do nothing
        };
    }

    @Bean
    @ConditionalOnMissingBean(SecurityTokenSetter.class)
    public SecurityTokenSetter noopSecurityTokenSetter() {
        return securityToken -> {
            // mocked bean: do nothing
        };
    }

    @Bean
    @ConditionalOnMissingBean(ServiceAccountIdSetter.class)
    public ServiceAccountIdSetter noopServiceAccountIdSetter() {
        return serviceAccountId -> {
            // mocked bean: do nothing
        };
    }

}
