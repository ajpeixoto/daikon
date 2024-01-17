// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

import org.talend.daikon.messages.MessageHeader;
import org.talend.daikon.messages.MessageIssuer;
import org.talend.daikon.messages.MessageTypes;

/**
 * Default implementation of {@link MessageHeaderFactory} that will delegate
 * retrieval of most information from different providers.
 * <p>
 * All providers are passed as constructor parameters.
 */
public class MessageHeaderFactoryImpl implements MessageHeaderFactory {

    private final IdGenerator idGenerator;

    private final ServiceInfoProvider serviceInfoProvider;

    private final TimestampProvider timestampProvider;

    private final UserProvider userProvider;

    private final TenantIdProvider tenantIdProvider;

    private final ServiceAccountIdProvider serviceAccountIdProvider;

    private final CorrelationIdProvider correlationIdProvider;

    private final SecurityTokenProvider securityTokenProvider;

    public MessageHeaderFactoryImpl(IdGenerator idGenerator, ServiceInfoProvider serviceInfoProvider,
            TimestampProvider timestampProvider, UserProvider userProvider, TenantIdProvider tenantIdProvider,
            CorrelationIdProvider correlationIdProvider, SecurityTokenProvider securityTokenProvider,
            ServiceAccountIdProvider serviceAccountIdProvider) {
        this.idGenerator = idGenerator;
        this.serviceInfoProvider = serviceInfoProvider;
        this.timestampProvider = timestampProvider;
        this.userProvider = userProvider;
        this.tenantIdProvider = tenantIdProvider;
        this.correlationIdProvider = correlationIdProvider;
        this.securityTokenProvider = securityTokenProvider;
        this.serviceAccountIdProvider = serviceAccountIdProvider;
    }

    @Override
    public MessageHeader createMessageHeader(MessageTypes type, String name) {
        return MessageHeader.newBuilder().setId(idGenerator.generateEventId())
                .setTimestamp(timestampProvider.getCurrentTimestamp()).setIssuer(createMessageIssuer()).setType(type)
                .setName(name).setTenantId(tenantIdProvider.getTenantId()).setUserId(userProvider.getUserId())
                .setCorrelationId(correlationIdProvider.getCorrelationId())
                .setSecurityToken(securityTokenProvider.getSecurityToken())
                .setServiceAccountId(serviceAccountIdProvider.getServiceAccountId()).build();
    }

    private MessageIssuer createMessageIssuer() {
        return MessageIssuer.newBuilder().setService(serviceInfoProvider.getServiceName())
                .setApplication(serviceInfoProvider.getApplicationName()).setVersion(serviceInfoProvider.getServiceVersion())
                .build();
    }
}
