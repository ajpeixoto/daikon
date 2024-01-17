// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.consumer;

import org.apache.avro.generic.IndexedRecord;
import org.talend.daikon.messages.MessageHeader;

public class ExecutionContextUpdaterImpl implements ExecutionContextUpdater {

    private final CorrelationIdSetter correlationIdSetter;

    private final TenantIdSetter tenantIdSetter;

    private final UserIdSetter userIdSetter;

    private final ServiceAccountIdSetter serviceAccountIdSetter;

    private final SecurityTokenSetter securityTokenSetter;

    private final MessageHeaderExtractor messageHeaderExtractor = new MessageHeaderExtractor();

    public ExecutionContextUpdaterImpl(CorrelationIdSetter correlationIdSetter, TenantIdSetter tenantIdSetter,
            UserIdSetter userIdSetter, SecurityTokenSetter securityTokenSetter, ServiceAccountIdSetter serviceAccountIdSetter) {
        this.correlationIdSetter = correlationIdSetter;
        this.tenantIdSetter = tenantIdSetter;
        this.userIdSetter = userIdSetter;
        this.securityTokenSetter = securityTokenSetter;
        this.serviceAccountIdSetter = serviceAccountIdSetter;
    }

    @Override
    public void updateExecutionContext(IndexedRecord indexedRecord) {
        final MessageHeader messageHeader = this.messageHeaderExtractor.extractHeader(indexedRecord);
        if (this.correlationIdSetter != null) {
            this.correlationIdSetter.setCurrentCorrelationId(messageHeader.getCorrelationId());
        }
        if (this.tenantIdSetter != null) {
            this.tenantIdSetter.setCurrentTenantId(messageHeader.getTenantId());
        }
        if (this.userIdSetter != null) {
            this.userIdSetter.setCurrentUserId(messageHeader.getUserId());
        }
        if (this.securityTokenSetter != null) {
            this.securityTokenSetter.setCurrentSecurityToken(messageHeader.getSecurityToken());
        }
        if (this.serviceAccountIdSetter != null) {
            this.serviceAccountIdSetter.setCurrentServiceAccountId(messageHeader.getServiceAccountId());
        }
    }
}
