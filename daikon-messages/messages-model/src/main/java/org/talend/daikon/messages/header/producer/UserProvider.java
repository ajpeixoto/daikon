// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

/**
 * Accesses the current request context and provides the current user identifier.
 */
public interface UserProvider {

    /**
     * @return the current user identifier or null if not authenticated.
     */
    String getUserId();

}
