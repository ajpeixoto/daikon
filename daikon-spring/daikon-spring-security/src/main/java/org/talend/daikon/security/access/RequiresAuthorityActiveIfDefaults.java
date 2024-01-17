// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.security.access;

import java.util.function.Function;

import org.springframework.context.ApplicationContext;

public class RequiresAuthorityActiveIfDefaults {

    private RequiresAuthorityActiveIfDefaults() {
    }

    /**
     * {@link RequiresAuthority} annotations are enabled by default
     * Return true Always true
     */
    public static class AlwaysTrue implements Function<ApplicationContext, Boolean> {

        @Override
        public Boolean apply(ApplicationContext applicationContext) {
            return Boolean.TRUE;
        }
    }
}
