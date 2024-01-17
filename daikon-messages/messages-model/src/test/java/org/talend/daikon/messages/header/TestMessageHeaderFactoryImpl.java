// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.avro.AvroRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.talend.daikon.messages.MessageHeader;
import org.talend.daikon.messages.MessageTypes;
import org.talend.daikon.messages.header.producer.CorrelationIdProvider;
import org.talend.daikon.messages.header.producer.IdGenerator;
import org.talend.daikon.messages.header.producer.MessageHeaderFactory;
import org.talend.daikon.messages.header.producer.MessageHeaderFactoryImpl;
import org.talend.daikon.messages.header.producer.SecurityTokenProvider;
import org.talend.daikon.messages.header.producer.ServiceAccountIdProvider;
import org.talend.daikon.messages.header.producer.ServiceInfoProvider;
import org.talend.daikon.messages.header.producer.TenantIdProvider;
import org.talend.daikon.messages.header.producer.TimestampProvider;
import org.talend.daikon.messages.header.producer.UserProvider;

public class TestMessageHeaderFactoryImpl {

    private String messageIdMock = "messageIdMock";

    private String applicationMock = "applicationMock";

    private String serviceMock = "serviceMock";

    private String versionMock = "versionMock";

    private long timestampMock = 1234L;

    private String userIdMock = "userIdMock";

    private String tenantIdMock = "tenantIdMock";

    private String serviceAccountIdMock = "serviceAccountIdMock";

    private String correlationIdMock = "correlationIdMock";

    private String securityTokenMock = "securityTokenMock";

    @BeforeEach
    public void setup() {
        messageIdMock = "messageIdMock";
    }

    @Test
    public void testCommandCreation() {
        String name = "commandName";
        MessageHeaderFactory factory = createFactory();
        MessageHeader commandHeader = factory.createMessageHeader(MessageTypes.COMMAND, name);
        assertMessageHeader(commandHeader, MessageTypes.COMMAND, name);
    }

    @Test
    public void testEventCreation() {
        String name = "eventName";
        MessageHeaderFactory factory = createFactory();
        MessageHeader eventHeader = factory.createMessageHeader(MessageTypes.EVENT, name);
        assertMessageHeader(eventHeader, MessageTypes.EVENT, name);
    }

    @Test
    public void testInvalidHeaderCreation() {
        assertThrows(AvroRuntimeException.class, () -> {
            String name = "eventName";
            messageIdMock = null;
            MessageHeaderFactory factory = createFactory();
            factory.createMessageHeader(MessageTypes.EVENT, name);
        });
    }

    private void assertMessageHeader(MessageHeader header, MessageTypes type, String name) {
        assertEquals(type, header.getType());
        assertEquals(name, header.getName());
        assertEquals(messageIdMock, header.getId());
        assertEquals(applicationMock, header.getIssuer().getApplication());
        assertEquals(serviceMock, header.getIssuer().getService());
        assertEquals(versionMock, header.getIssuer().getVersion());
        assertEquals(timestampMock, header.getTimestamp(), 0);
        assertEquals(userIdMock, header.getUserId());
        assertEquals(tenantIdMock, header.getTenantId());
        assertEquals(correlationIdMock, header.getCorrelationId());
        assertEquals(securityTokenMock, header.getSecurityToken());
    }

    private MessageHeaderFactory createFactory() {
        return new MessageHeaderFactoryImpl(idGenerator(), serviceInfoProvider(), timestampProvider(), userProvider(),
                tenantIdProvider(), correlationIdProvider(), securityTokenProvider(), serviceAccountIdProvider());
    }

    private IdGenerator idGenerator() {
        IdGenerator idGenerator = Mockito.mock(IdGenerator.class);
        Mockito.when(idGenerator.generateEventId()).thenReturn(messageIdMock);
        return idGenerator;
    }

    private ServiceInfoProvider serviceInfoProvider() {
        ServiceInfoProvider serviceInfoProvider = Mockito.mock(ServiceInfoProvider.class);
        Mockito.when(serviceInfoProvider.getApplicationName()).thenReturn(applicationMock);
        Mockito.when(serviceInfoProvider.getServiceName()).thenReturn(serviceMock);
        Mockito.when(serviceInfoProvider.getServiceVersion()).thenReturn(versionMock);
        return serviceInfoProvider;
    }

    private TimestampProvider timestampProvider() {
        TimestampProvider timestampProvider = Mockito.mock(TimestampProvider.class);
        Mockito.when(timestampProvider.getCurrentTimestamp()).thenReturn(timestampMock);
        return timestampProvider;
    }

    private UserProvider userProvider() {
        UserProvider userProvider = Mockito.mock(UserProvider.class);
        Mockito.when(userProvider.getUserId()).thenReturn(userIdMock);
        return userProvider;
    }

    private TenantIdProvider tenantIdProvider() {
        TenantIdProvider tenantIdProvider = Mockito.mock(TenantIdProvider.class);
        Mockito.when(tenantIdProvider.getTenantId()).thenReturn(tenantIdMock);
        return tenantIdProvider;
    }

    private ServiceAccountIdProvider serviceAccountIdProvider() {
        ServiceAccountIdProvider serviceAccountIdProvider = Mockito.mock(ServiceAccountIdProvider.class);
        Mockito.when(serviceAccountIdProvider.getServiceAccountId()).thenReturn(serviceAccountIdMock);
        return serviceAccountIdProvider;
    }

    private CorrelationIdProvider correlationIdProvider() {
        CorrelationIdProvider correlationIdProvider = Mockito.mock(CorrelationIdProvider.class);
        Mockito.when(correlationIdProvider.getCorrelationId()).thenReturn(correlationIdMock);
        return correlationIdProvider;
    }

    private SecurityTokenProvider securityTokenProvider() {
        SecurityTokenProvider securityTokenProvider = Mockito.mock(SecurityTokenProvider.class);
        Mockito.when(securityTokenProvider.getSecurityToken()).thenReturn(securityTokenMock);
        return securityTokenProvider;
    }

}
