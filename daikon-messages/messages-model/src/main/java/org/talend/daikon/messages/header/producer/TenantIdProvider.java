// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

/**
 * Accesses the current request context and provides the identifier of the current tenant.
 */
public interface TenantIdProvider {

    /**
     * @return the current tenant identifier or null if not in a multi-tenant context
     */
    String getTenantId();

}
