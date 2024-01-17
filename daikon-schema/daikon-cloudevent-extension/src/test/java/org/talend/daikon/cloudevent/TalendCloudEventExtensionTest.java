// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.cloudevent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.provider.ExtensionProvider;

public class TalendCloudEventExtensionTest {

    @Test
    public void writeExtension() {
        String randomUUID = UUID.randomUUID().toString();
        TalendCloudEventExtension talendCloudEventExtension = TalendCloudEventExtension.builder().correlationId("correlationId")
                .tenantId(randomUUID).ownerId(randomUUID).clientId(randomUUID).userId(randomUUID).description(randomUUID).build();

        CloudEvent event = CloudEventBuilder.v03().withId("aaa").withSource(URI.create("http://localhost")).withType("example")
                .withExtension(talendCloudEventExtension).build();

        assertEquals(randomUUID, event.getExtension(TalendCloudEventExtension.TENANTID));
        assertEquals("correlationId", event.getExtension(TalendCloudEventExtension.CORRELATIONID));
        assertEquals(randomUUID, event.getExtension(TalendCloudEventExtension.OWNERID));
        assertEquals(randomUUID, event.getExtension(TalendCloudEventExtension.USERID));
        assertEquals(randomUUID, event.getExtension(TalendCloudEventExtension.CLIENTID));
        assertEquals(randomUUID, event.getExtension(TalendCloudEventExtension.DESCRIPTION));
    }

    @Test
    public void parseExtension() {
        String randomUUID = UUID.randomUUID().toString();

        CloudEvent event = CloudEventBuilder.v03().withId("aaa").withSource(URI.create("http://localhost")).withType("example")
                .withExtension(TalendCloudEventExtension.TENANTID, randomUUID)
                .withExtension(TalendCloudEventExtension.CORRELATIONID, "correlationId")
                .withExtension(TalendCloudEventExtension.OWNERID, randomUUID)
                .withExtension(TalendCloudEventExtension.CLIENTID, randomUUID)
                .withExtension(TalendCloudEventExtension.USERID, randomUUID)
                .withExtension(TalendCloudEventExtension.DESCRIPTION, randomUUID).build();

        // register extension
        ExtensionProvider.getInstance().registerExtension(TalendCloudEventExtension.class, TalendCloudEventExtension::new);

        TalendCloudEventExtension talendCloudEventExtension = ExtensionProvider.getInstance()
                .parseExtension(TalendCloudEventExtension.class, event);

        assertNotNull(talendCloudEventExtension);
        assertEquals("correlationId", talendCloudEventExtension.getCorrelationId());
        assertEquals(randomUUID, talendCloudEventExtension.getTenantId());
        assertEquals(randomUUID, talendCloudEventExtension.getOwnerId());
        assertEquals(randomUUID, talendCloudEventExtension.getUserId());
        assertEquals(randomUUID, talendCloudEventExtension.getClientId());
        assertEquals(randomUUID, talendCloudEventExtension.getDescription());

    }
}
