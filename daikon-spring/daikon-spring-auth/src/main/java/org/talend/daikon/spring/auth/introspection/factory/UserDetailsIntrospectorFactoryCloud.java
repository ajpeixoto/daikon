// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.auth.introspection.factory;

import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.talend.daikon.spring.auth.introspection.AuthUserDetailsConverterIntrospector;

public class UserDetailsIntrospectorFactoryCloud implements UserDetailsIntrospectorFactory {

    public OpaqueTokenIntrospector build(OpaqueTokenIntrospector delegate) {
        return new AuthUserDetailsConverterIntrospector(delegate);
    }

}
