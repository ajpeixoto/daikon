// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.envelope;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestMessageConverterRegistryImpl {

    private MessageConverterRegistryImpl messageConverterRegistry = new MessageConverterRegistryImpl();

    @Test
    public void testGetMessageConverter() {
        messageConverterRegistry.registerConverter("format1", new Message1Converter());
        messageConverterRegistry.registerConverter("format2", new Message2Converter());

        assertEquals(Message1Converter.class, messageConverterRegistry.getMessageConverter("format1").getClass());
        assertEquals(Message2Converter.class, messageConverterRegistry.getMessageConverter("format2").getClass());

    }

    private static class Message1Converter implements MessageConverter {

        @Override
        public <T> T deserialize(String content, Class<T> clazz) {
            return null;
        }

        @Override
        public <T> String serialize(T content) {
            return "";
        }
    }

    private static class Message2Converter implements MessageConverter {

        @Override
        public <T> T deserialize(String content, Class<T> clazz) {
            return null;
        }

        @Override
        public <T> String serialize(T content) {
            return "";
        }
    }

}
