// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

/**
 * Accesses the current security context and provides the current security token
 */
@FunctionalInterface
public interface SecurityTokenProvider {

    /**
     * @return the current security token or null if not authenticated context
     */
    String getSecurityToken();
}
