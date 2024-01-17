// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.audit.logs.api;

public class NoOpAuditUserProvider implements AuditUserProvider {

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getUserEmail() {
        return null;
    }

    @Override
    public String getAccountId() {
        return null;
    }
}
