// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.envelope;

import org.talend.daikon.messages.MessageEnvelope;
import org.talend.daikon.messages.MessageTypes;

/**
 * The main entry point of the message envelope API allowing to
 *
 * - wrap an arbitrary message into a {@link MessageEnvelope} including
 * {@link org.talend.daikon.messages.MessageHeader} creation and payload serialization
 *
 * - unwrap a {@link MessageEnvelope} and retrieve the actual message that was wrapped in the envelope.
 */
public interface MessageEnvelopeHandler {

    /**
     * Creates a new message envelop for the provided payload.
     * 
     * @param type the message type
     * @param messageName the name message name
     * @param payload the message payload to wrap
     * @param format the serialization format
     * @param <T> the message payload type
     * @return the envelope
     */
    <T> MessageEnvelope wrap(MessageTypes type, String messageName, T payload, String format);

    /**
     * Retrieves the actual message that is wrapped in the provided envelope
     * 
     * @param envelop the enveloped containing the message
     * @param <T> the message type
     * @return the message
     */
    <T> T unwrap(MessageEnvelope envelop, Class<T> clazz);

}
