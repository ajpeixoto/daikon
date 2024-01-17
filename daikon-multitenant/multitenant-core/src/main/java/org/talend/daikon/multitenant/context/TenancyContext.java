// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.context;

import java.io.Serializable;
import java.util.Optional;

import org.talend.daikon.multitenant.core.Tenant;

/**
 * a context in which tenancy can be defined
 *
 * @author Clint Morgan (Tasktop Technologies Inc.)
 */
public interface TenancyContext extends Serializable {

    /**
     * Obtains the current tenant.
     *
     * @return the <code>Tenant</code> in the current running context (will never be null).
     * @throws NoCurrentTenantException if no tenancy information is available.
     */
    Tenant getTenant();

    /**
     * Obtains the current tenant.
     *
     * @return the <code>Tenant</code> in the current running context.
     */
    Optional<Tenant> getOptionalTenant();

    /**
     * Changes the current tenant, or removes the tenancy information.
     *
     * @param tenant the new <code>Tenant</code>, or <code>null</code> if no further tenancy information should be stored
     */
    void setTenant(Tenant tenant);
}
