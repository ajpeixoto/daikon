// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

/**
 * Accesses the current request context and provides the current service account identifier.
 */
public interface ServiceAccountIdProvider {

    /**
     * @return the current service account identifier or null if no service account.
     */
    String getServiceAccountId();

}
