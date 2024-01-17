// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.envelope;

import org.talend.daikon.messages.MessageEnvelope;
import org.talend.daikon.messages.MessageHeader;
import org.talend.daikon.messages.MessagePayload;
import org.talend.daikon.messages.MessageTypes;
import org.talend.daikon.messages.header.producer.MessageHeaderFactory;

public class MessageEnvelopeHandlerImpl implements MessageEnvelopeHandler {

    private final MessageConverterRegistry messageConverterRegistry;

    private final MessageHeaderFactory messageHeaderFactory;

    public MessageEnvelopeHandlerImpl(MessageConverterRegistry messageConverterRegistry,
            MessageHeaderFactory messageHeaderFactory) {
        this.messageConverterRegistry = messageConverterRegistry;
        this.messageHeaderFactory = messageHeaderFactory;
    }

    @Override
    public <T> MessageEnvelope wrap(MessageTypes type, String messageName, T payload, String format) {
        return MessageEnvelope.newBuilder().setHeader(createMessageHeader(type, messageName))
                .setPayload(createMessagePayload(payload, format)).build();
    }

    @Override
    public <T> T unwrap(MessageEnvelope envelop, Class<T> clazz) {
        MessageConverter messageConverter = this.getMessageConverter(envelop.getPayload().getFormat());
        String content = envelop.getPayload().getContent();
        return messageConverter.deserialize(content, clazz);
    }

    private <T> MessagePayload createMessagePayload(T payload, String format) {
        MessageConverter messageConverter = this.getMessageConverter(format);
        String content = messageConverter.serialize(payload);
        return MessagePayload.newBuilder().setFormat(format).setContent(content).build();
    }

    private MessageHeader createMessageHeader(MessageTypes type, String messageName) {
        return messageHeaderFactory.createMessageHeader(type, messageName);
    }

    private MessageConverter getMessageConverter(String format) {
        return this.messageConverterRegistry.getMessageConverter(format);
    }
}
