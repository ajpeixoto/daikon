// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.spring.producer;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.AvroRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.talend.daikon.messages.envelope.MessageConverter;
import org.talend.daikon.messages.envelope.MessageConverterRegistry;
import org.talend.daikon.messages.envelope.MessageConverterRegistryImpl;
import org.talend.daikon.messages.envelope.MessageEnvelopeHandler;
import org.talend.daikon.messages.envelope.MessageEnvelopeHandlerImpl;
import org.talend.daikon.messages.header.producer.*;

@AutoConfiguration
@AutoConfigureBefore({ DefaultProducerProvidersConfiguration.class })
public class MessageHeaderFactoryConfiguration {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private TimestampProvider timestampProvider;

    @Autowired
    private ServiceInfoProvider serviceInfoProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private TenantIdProvider tenantIdProvider;

    @Autowired
    private CorrelationIdProvider correlationIdProvider;

    @Autowired
    private SecurityTokenProvider securityTokenProvider;

    @Autowired
    private ServiceAccountIdProvider serviceAccountIdProvider;

    @Bean
    public MessageHeaderFactory messageHeaderFactory() {
        return new MessageHeaderFactoryImpl(idGenerator, serviceInfoProvider, timestampProvider, userProvider, tenantIdProvider,
                correlationIdProvider, securityTokenProvider, serviceAccountIdProvider);
    }

    @Bean
    public MessageConverterRegistry messageConverterRegistry() {
        MessageConverterRegistryImpl messageConverterRegistry = new MessageConverterRegistryImpl();
        messageConverterRegistry.registerConverter("json", new MessageConverter() {

            private final ObjectMapper objectMapper = new ObjectMapper();

            @Override
            public <T> T deserialize(String content, Class<T> clazz) {
                try {
                    return objectMapper.readValue(content, clazz);
                } catch (IOException e) {
                    throw new AvroRuntimeException("", e);
                }
            }

            @Override
            public <T> String serialize(T content) {
                try {
                    return objectMapper.writeValueAsString(content);
                } catch (IOException e) {
                    throw new AvroRuntimeException("", e);
                }
            }
        });
        return messageConverterRegistry;
    }

    @Bean
    public MessageEnvelopeHandler messageEnvelopeHandler(MessageConverterRegistry messageConverterRegistry,
            MessageHeaderFactory messageHeaderFactory) {
        return new MessageEnvelopeHandlerImpl(messageConverterRegistry, messageHeaderFactory);
    }

}
