// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

/**
 * A class able to generate event identifiers.
 */
@FunctionalInterface
public interface IdGenerator {

    /**
     * @return a new event identifier
     */
    String generateEventId();

}
