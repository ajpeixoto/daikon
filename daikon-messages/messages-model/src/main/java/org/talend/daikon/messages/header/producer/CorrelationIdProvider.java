// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

/**
 * Accesses the current request identifier and provides the current correlation id.
 *
 * This implementation should generate a new correlation id if the current request has no correlation id.
 */
@FunctionalInterface
public interface CorrelationIdProvider {

    /**
     * @return the current correlation id or generates a new one.
     */
    String getCorrelationId();

}
