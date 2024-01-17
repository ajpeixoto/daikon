// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.messages.spring.consumer;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.talend.daikon.messages.header.consumer.CorrelationIdSetter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;

@AutoConfiguration
@ConditionalOnClass({ Tracer.class })
@AutoConfigureBefore({ DefaultConsumerSettersConfiguration.class })
@ConditionalOnProperty(prefix = "management.tracing", name = "enabled", matchIfMissing = true)
public class SpringSettersConfiguration {

    @Bean
    public CorrelationIdSetter correlationIdSetter(Tracer tracer) {
        return traceId -> {
            final TraceContext context = tracer.traceContextBuilder().traceId(traceId).build();
            final Span content = tracer.spanBuilder().setParent(context).start();
            tracer.nextSpan(content);
        };
    }

}
