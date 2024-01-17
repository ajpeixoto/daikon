// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.envelope;

/**
 * A registry of {@link MessageConverter}.
 */
public interface MessageConverterRegistry {

    /**
     * @return a {@link MessageConverter} for the provided format
     *
     * @param format the format
     *
     * @throws IllegalArgumentException if the provided format is not supported
     *
     */
    MessageConverter getMessageConverter(String format);

}
