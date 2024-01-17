// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

import java.util.UUID;

/**
 * A default implementation of {@link IdGenerator} using Java UUID generator
 */
public class LocalIdGenerator implements IdGenerator {

    @Override
    public String generateEventId() {
        return UUID.randomUUID().toString();
    }
}
