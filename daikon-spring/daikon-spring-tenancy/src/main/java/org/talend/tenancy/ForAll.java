// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tenancy;

import java.util.function.Supplier;

/**
 * An interface to abstract repetitive actions (to be performed on all tenants for example).
 */
public interface ForAll {

    /**
     * Execute the provided <code>runnable</code> for all tenants.
     *
     * @param runnable The {@link Runnable} to execute.
     * @param condition A {@link Supplier} to execute to decide whether to execute <code>runnable</code> or not.
     */
    void execute(final Supplier<Boolean> condition, Runnable runnable);

    /**
     * @return A {@link ForAllConditionBuilder builder} for building conditions to {@link #execute(Supplier, Runnable)}.
     */
    ForAllConditionBuilder condition();

    /**
     * Execute the provided <code>runnable</code> for all tenants.
     *
     * @param runnable The {@link Runnable} to execute.
     */
    default void execute(Runnable runnable) {
        execute(() -> true, runnable);
    }

    /**
     * A builder for {@link #execute(Supplier, Runnable)}.
     */
    interface ForAllConditionBuilder {

        /**
         * <p>
         * Checks if a bean can operate in current context. For example, implementation may check tenancy information
         * has all required configuration.
         * </p>
         *
         * @param bean The Spring bean to be check.
         * @return A check if bean can operate in current context.
         */
        Supplier<Boolean> operational(Object bean);

    }

}
