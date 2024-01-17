// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.envelope;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bguillon on 14/12/2017.
 */
public class MessageConverterRegistryImpl implements MessageConverterRegistry {

    private final Map<String, MessageConverter> messageConverters = new HashMap<>();

    public void registerConverter(String format, MessageConverter converter) {
        this.messageConverters.put(format, converter);
    }

    @Override
    public MessageConverter getMessageConverter(String format) {
        return messageConverters.get(format);
    }
}
