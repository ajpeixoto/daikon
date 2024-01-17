// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.spring.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.talend.daikon.messages.header.producer.*;

@AutoConfiguration
public class DefaultProducerProvidersConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.service.name}")
    private String serviceName;

    @Value("${spring.application.version:}")
    private String appVersion;

    @Value("${org.talend.daikon.messages.defaultTenantId:}")
    private String defaultTenantId;

    @Value("${org.talend.daikon.messages.defaultUserId:}")
    private String defaultUserId;

    @Value("${org.talend.daikon.messages.defaultServiceAccountId:}")
    private String defaultServiceAccountId;

    @Bean
    @ConditionalOnMissingBean(ServiceInfoProvider.class)
    public ServiceInfoProvider defaultServiceInfoProvider() {
        return new ServiceInfoProvider() {

            @Override
            public String getServiceName() {
                return serviceName;
            }

            @Override
            public String getServiceVersion() {
                return appVersion;
            }

            @Override
            public String getApplicationName() {
                return appName;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(IdGenerator.class)
    public IdGenerator defaultEventIdGenerator() {
        return new LocalIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(TimestampProvider.class)
    public TimestampProvider defaultTimestampProvider() {
        return new LocalTimestampProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserProvider defaultUserProvider() {
        return new UserProvider() {

            @Override
            public String getUserId() {
                return defaultUserId;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(TenantIdProvider.class)
    public TenantIdProvider defaultTenantIdProvider() {
        return new TenantIdProvider() {

            @Override
            public String getTenantId() {
                return defaultTenantId;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(ServiceAccountIdProvider.class)
    public ServiceAccountIdProvider defaultServiceAccountIdProvider() {
        return new ServiceAccountIdProvider() {

            @Override
            public String getServiceAccountId() {
                return defaultServiceAccountId;
            }
        };
    }
}
