// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security.tenant;

import java.util.function.Function;

import org.talend.daikon.multitenant.context.DefaultTenancyContext;
import org.talend.daikon.multitenant.context.TenancyContext;
import org.talend.daikon.multitenant.core.Tenant;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

/**
 * Inspired by {@link org.springframework.security.core.context.ReactiveSecurityContextHolder}
 */
public class ReactiveTenancyContextHolder {

    public static final Class<?> TENANCY_CONTEXT_KEY = TenancyContext.class;

    /**
     * Gets the {@code Mono<TenancyContext>} from Reactor {@link Context}
     *
     * @return the {@code Mono<TenancyContext>}
     */
    public static Mono<TenancyContext> getContext() {
        return Mono.deferContextual(Mono::just).filter(c -> c.hasKey(TENANCY_CONTEXT_KEY))
                .flatMap(c -> c.<Mono<TenancyContext>> get(TENANCY_CONTEXT_KEY));
    }

    /**
     * Clears the {@code Mono<TenancyContext>} from Reactor {@link Context}
     *
     * @return Return a {@code Mono<Void>} which only replays complete and error signals
     * from clearing the context.
     */
    public static Function<Context, Context> clearContext() {
        return context -> context.delete(TENANCY_CONTEXT_KEY);
    }

    /**
     * Creates a Reactor {@link Context} that contains the {@code Mono<TenancyContext>}
     * that can be merged into another {@link Context}
     *
     * @param TenancyContext the {@code Mono<TenancyContext>} to set in the returned
     * Reactor {@link Context}
     * @return a Reactor {@link Context} that contains the {@code Mono<TenancyContext>}
     */
    public static ContextView withTenancyContext(Mono<? extends TenancyContext> TenancyContext) {
        return Context.of(TENANCY_CONTEXT_KEY, TenancyContext);
    }

    /**
     * A shortcut for {@link #withTenancyContext(Mono)}
     *
     * @param tenant the {@link Tenant} to be used
     * @return a Reactor {@link Context} that contains the {@code Mono<TenancyContext>}
     */
    public static ContextView withTenant(Tenant tenant) {
        TenancyContext tc = new DefaultTenancyContext();
        tc.setTenant(tenant);
        return withTenancyContext(Mono.just(tc));
    }
}
