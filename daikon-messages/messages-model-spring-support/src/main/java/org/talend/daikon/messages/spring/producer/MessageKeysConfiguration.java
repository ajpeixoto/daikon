// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.spring.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.talend.daikon.messages.header.producer.TenantIdProvider;
import org.talend.daikon.messages.keys.MessageKeyFactory;
import org.talend.daikon.messages.keys.MessageKeyFactoryImpl;

@AutoConfiguration
@AutoConfigureBefore({ DefaultProducerProvidersConfiguration.class })
public class MessageKeysConfiguration {

    @Bean
    public MessageKeyFactory messageKeyFactory(@Autowired TenantIdProvider tenantIdProvider) {
        return new MessageKeyFactoryImpl(tenantIdProvider);
    }

}
