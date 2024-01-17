// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

/**
 * Provides the current timestamp
 */
public interface TimestampProvider {

    /**
     * @return the current timestamp (number of milliseconds since epoch)
     */
    long getCurrentTimestamp();

}
