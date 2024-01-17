// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.envelope;

/**
 * Converts a message payload to string and the other way around.
 */
public interface MessageConverter {

    /**
     * Deserializes the provided message payload
     *
     * @param content the provided content
     * @param <T> the expected type
     * @return the deserialized message payload
     */
    <T> T deserialize(String content, Class<T> clazz);

    /**
     * Serializes the provided message payload to string
     *
     * @param content the message payload
     * @param <T> the message payload type
     * @return the serialized form of the payload.
     */
    <T> String serialize(T content);

}
