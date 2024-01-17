// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.multitenant.concurrent;

import java.util.concurrent.Callable;

import org.talend.daikon.multitenant.context.DefaultTenancyContext;
import org.talend.daikon.multitenant.context.TenancyContext;
import org.talend.daikon.multitenant.context.TenancyContextHolder;
import org.talend.daikon.multitenant.core.Tenant;

/**
 * A callable that sets the tenancy context for the duration of the call.
 * 
 * @author David Green (Tasktop Technologies Inc.)
 */
public abstract class TenancyCallable<T> implements Callable<T> {

    private final Tenant tenant;

    /**
     * create the callable with the given tenant as the context
     */
    public TenancyCallable(Tenant tenant) {
        this.tenant = tenant;
    }

    /**
     * create the callable with the current context tenant as the context of the callable
     */
    public TenancyCallable() {
        this(TenancyContextHolder.getContext().getTenant());
    }

    @Override
    public final T call() throws Exception {
        final TenancyContext previousTenancyContext = TenancyContextHolder.getContext();

        DefaultTenancyContext context = new DefaultTenancyContext();
        context.setTenant(tenant);
        TenancyContextHolder.setContext(context);
        try {
            return callAsTenant();
        } finally {
            TenancyContextHolder.setContext(previousTenancyContext);
        }
    }

    protected abstract T callAsTenant() throws Exception;
}