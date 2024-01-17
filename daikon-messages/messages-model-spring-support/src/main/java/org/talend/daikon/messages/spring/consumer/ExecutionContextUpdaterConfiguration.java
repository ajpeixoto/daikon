// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.spring.consumer;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.talend.daikon.messages.header.consumer.CorrelationIdSetter;
import org.talend.daikon.messages.header.consumer.ExecutionContextUpdater;
import org.talend.daikon.messages.header.consumer.ExecutionContextUpdaterImpl;
import org.talend.daikon.messages.header.consumer.SecurityTokenSetter;
import org.talend.daikon.messages.header.consumer.ServiceAccountIdSetter;
import org.talend.daikon.messages.header.consumer.TenantIdSetter;
import org.talend.daikon.messages.header.consumer.UserIdSetter;

@AutoConfiguration
@AutoConfigureBefore({ DefaultConsumerSettersConfiguration.class })
public class ExecutionContextUpdaterConfiguration {

    @Bean
    public ExecutionContextUpdater executionContextUpdater(CorrelationIdSetter correlationIdSetter, TenantIdSetter tenantIdSetter,
            SecurityTokenSetter securityTokenSetter, UserIdSetter userIdSetter, ServiceAccountIdSetter serviceAccountIdSetter) {
        return new ExecutionContextUpdaterImpl(correlationIdSetter, tenantIdSetter, userIdSetter, securityTokenSetter,
                serviceAccountIdSetter);
    }

}
