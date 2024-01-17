// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.spring.auth.introspection.factory;

import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

public interface UserDetailsIntrospectorFactory {

    OpaqueTokenIntrospector build(OpaqueTokenIntrospector delegate);

}
