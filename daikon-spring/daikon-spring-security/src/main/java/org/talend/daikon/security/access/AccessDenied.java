// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security.access;

import java.lang.reflect.Method;

/**
 * An interface to define {@link RequiresAuthority#onDeny()} behavior.
 * Implementations must have a no-argument constructor as it is instantiated through {@link Class#newInstance()}.
 *
 * @param <T> The return type of the annotated method.
 */
@FunctionalInterface
public interface AccessDenied<T> {

    /**
     * @return The expected value for the annotated method.
     * @param requirements The requirements defined in a {@link RequiresAuthority} annotation.
     * @param method The method protected with the {@link RequiresAuthority} annotation.
     * @param args The arguments used for <code>method</code>.
     */
    T onDeny(RequiresAuthority requirements, Method method, Object[] args);
}
