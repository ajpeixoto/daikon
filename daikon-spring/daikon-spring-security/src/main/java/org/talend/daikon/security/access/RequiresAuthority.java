// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.GrantedAuthority;

/**
 * A helper annotation to indicate what authorities are needed to execute method.
 *
 * @see RequiresAuthorityAspect
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ ElementType.METHOD })
public @interface RequiresAuthority {

    /**
     * @return The authority list (as returned by {@link GrantedAuthority#getAuthority()} needed to execute method.
     * Value is
     * ignored if {@link #authority()} is defined.
     */
    String[] value() default {};

    /**
     * @return The authority (as returned by {@link GrantedAuthority#getAuthority()} needed to execute method. This has
     * precedence over {@link #value()}.
     */
    String[] authority() default {};

    /**
     * @return A predicate to know if the authority list should be checked.
     */
    Class<? extends Function<ApplicationContext, Boolean>> activeIf() default RequiresAuthorityActiveIfDefaults.AlwaysTrue.class;

    /**
     * @return A {@link AccessDenied} implementation to handle access denials.
     */
    Class<? extends AccessDenied> onDeny() default AccessDeniedDefaults.ThrowException.class;
}
