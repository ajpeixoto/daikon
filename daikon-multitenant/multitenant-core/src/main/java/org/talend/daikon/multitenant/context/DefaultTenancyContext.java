// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.context;

import java.util.Optional;

import org.talend.daikon.multitenant.core.Tenant;

/**
 * Default implementation of {@link TenancyContext}.
 *
 * @author Clint Morgan (Tasktop Technologies Inc.)
 */
public class DefaultTenancyContext implements TenancyContext {

    private static final long serialVersionUID = 1L;

    private Tenant tenant;

    @Override
    public Tenant getTenant() {
        if (tenant == null) {
            throw new NoCurrentTenantException("No tenant in running context");
        }
        return tenant;
    }

    @Override
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public Optional<Tenant> getOptionalTenant() {
        return Optional.ofNullable(tenant);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DefaultTenancyContext other = (DefaultTenancyContext) obj;
        if (tenant == null) {
            if (other.tenant != null) {
                return false;
            }
        } else if (!tenant.equals(other.tenant)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DefaultTenancyContext [tenant=" + tenant + "]";
    }

}
