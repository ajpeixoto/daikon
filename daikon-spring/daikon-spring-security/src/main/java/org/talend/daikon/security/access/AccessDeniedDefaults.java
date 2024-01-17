// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security.access;

import static java.util.Collections.emptyList;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;

/**
 * Default/Helper implementations for {@link AccessDenied}
 */
public class AccessDeniedDefaults {

    private AccessDeniedDefaults() {
    }

    /**
     * Throw a {@link TalendRuntimeException} on denied access with a HTTP 403.
     * 
     * @see CommonErrorCodes#INSUFFICIENT_AUTHORITY
     */
    public static class ThrowException implements AccessDenied<Object> { // NOSONAR

        @Override
        public Object onDeny(RequiresAuthority requirements, Method method, Object[] args) {
            throw new TalendRuntimeException(CommonErrorCodes.INSUFFICIENT_AUTHORITY);
        }
    }

    /**
     * Return empty string in case of denied access.
     */
    public static class EmptyString implements AccessDenied<String> {

        @Override
        public String onDeny(RequiresAuthority requirements, Method method, Object[] args) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * Return empty list in case of denied access.
     */
    public static class EmptyList implements AccessDenied<List> {

        @Override
        public List onDeny(RequiresAuthority requirements, Method method, Object[] args) {
            return emptyList();
        }
    }

    /**
     * Return false in case of denied access.
     */
    public static class False implements AccessDenied<Boolean> {

        @Override
        public Boolean onDeny(RequiresAuthority requirements, Method method, Object[] args) {
            return false;
        }
    }

    /**
     * A {@link AccessDenied} implementation to return an empty stream on denied access.
     */
    public static class EmptyStream implements AccessDenied<Stream> {

        @Override
        public Stream onDeny(RequiresAuthority requiresAuthority, Method method, Object[] objects) {
            return Stream.empty();
        }
    }

}
