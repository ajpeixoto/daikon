// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

/**
 * A default implementation of {@link TimestampProvider} using the Java API.
 */
public class LocalTimestampProvider implements TimestampProvider {

    @Override
    public long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
}
