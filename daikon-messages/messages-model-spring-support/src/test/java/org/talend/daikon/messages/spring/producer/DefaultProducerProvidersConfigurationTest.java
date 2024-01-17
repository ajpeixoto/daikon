// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.spring.producer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.talend.daikon.messages.header.producer.TenantIdProvider;
import org.talend.daikon.messages.spring.test.utils.MessageTestApp;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MessageTestApp.class)
public class DefaultProducerProvidersConfigurationTest {

    @Autowired
    private TenantIdProvider tenantIdProvider;

    @Autowired
    private ApplicationContext context;

    @Test
    public void noBeanOverriding() {
        assertThat(tenantIdProvider).isNotNull();
        String[] beans = context.getBeanNamesForType(TenantIdProvider.class);
        assertThat(beans).hasSize(1);
        assertThat(beans[0]).isEqualTo("defaultTenantIdProvider");
    }
}
