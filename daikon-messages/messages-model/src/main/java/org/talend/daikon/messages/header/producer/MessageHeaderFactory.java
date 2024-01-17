// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.header.producer;

import org.talend.daikon.messages.MessageHeader;
import org.talend.daikon.messages.MessageTypes;

/**
 * A factory for normalized {@link MessageHeader}
 */
@FunctionalInterface
public interface MessageHeaderFactory {

    /**
     * Creates a new message header for the provided message type and name
     *
     * @param type the message type
     * @param messageName the message name
     * @return the newly created message header
     */
    MessageHeader createMessageHeader(MessageTypes type, String messageName);

}
