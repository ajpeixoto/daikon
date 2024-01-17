// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.logging.audit.logback;

import static java.util.Objects.requireNonNull;
import static org.talend.logging.audit.impl.AuditConfiguration.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.talend.daikon.logging.event.layout.LogbackJSONLayout;
import org.talend.logging.audit.AuditLoggingException;
import org.talend.logging.audit.LogAppenders;
import org.talend.logging.audit.impl.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.LoggingEventVO;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.net.AbstractSocketAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.spi.PreSerializationTransformer;
import ch.qos.logback.core.util.FileSize;

/**
 *
 */
public final class LogbackConfigurer {

    public static final String LOG_AUDIT_SOCKET_APPENDER_NAME = "auditSocketAppender";

    private LogbackConfigurer() {
    }

    public static void configure(AuditConfigurationMap config, LoggerContext loggerContext) {
        final Logger logger = loggerContext.getLogger(ROOT_LOGGER.getString(config));
        logger.setAdditive(false);

        final LogAppendersSet appendersSet = LOG_APPENDER.getValue(config, LogAppendersSet.class);

        if (appendersSet == null || appendersSet.isEmpty()) {
            throw new AuditLoggingException("No audit appenders configured.");
        }

        if (appendersSet.size() > 1 && appendersSet.contains(LogAppenders.NONE)) {
            throw new AuditLoggingException("Invalid configuration: none appender is used with other simultaneously.");
        }

        for (LogAppenders appender : appendersSet) {
            switch (appender) {
            case FILE -> logger.addAppender(rollingFileAppender(config, loggerContext));
            case SOCKET -> logger.addAppender(socketAppender(config, loggerContext));
            case CONSOLE -> logger.addAppender(consoleAppender(config, loggerContext));
            case HTTP -> logger.addAppender(httpAppender(config, loggerContext));
            case NONE -> logger.setLevel(Level.OFF);
            default -> throw new AuditLoggingException("Unknown appender " + appender);
            }
        }
    }

    static Appender<ILoggingEvent> socketAppender(final AuditConfigurationMap config, final LoggerContext loggerContext) {
        final String app = requireNonNull(APPENDER_SOCKET_APPLICATION.getString(config),
                "application must be set for logback socket appender");
        final AbstractSocketAppender<ILoggingEvent> appender = new AbstractSocketAppender<>() {

            @Override
            protected void postProcessEvent(final ILoggingEvent event) {
                // no-op
            }

            @Override
            protected void append(final ILoggingEvent event) {
                event.prepareForDeferredProcessing();
                super.append(event);
            }

            @Override
            protected PreSerializationTransformer<ILoggingEvent> getPST() {
                return e -> {
                    final Map<String, String> mdc = new HashMap<>();
                    mdc.put("application", app);
                    mdc.putAll(e.getMDCPropertyMap());

                    final LoggingEvent copy = new LoggingEvent();
                    copy.setLoggerName(e.getLoggerName());
                    copy.setLoggerContextRemoteView(e.getLoggerContextVO());
                    copy.setThreadName(e.getThreadName());
                    copy.setLevel(e.getLevel());
                    copy.setMessage(e.getMessage());
                    copy.setArgumentArray(e.getArgumentArray());
                    copy.addMarker(e.getMarker());
                    copy.setMDCPropertyMap(mdc);
                    copy.setTimeStamp(e.getTimeStamp());
                    if (e.getThrowableProxy() instanceof ThrowableProxy throwableProxy) {
                        copy.setThrowableProxy(throwableProxy);
                    }
                    if (copy.hasCallerData()) {
                        copy.setCallerData(e.getCallerData());
                    }
                    return LoggingEventVO.build(copy);
                };
            }
        };
        appender.setContext(loggerContext);
        appender.setName(LOG_AUDIT_SOCKET_APPENDER_NAME);
        appender.setRemoteHost(APPENDER_SOCKET_HOST.getString(config));
        appender.setPort(APPENDER_SOCKET_PORT.getInteger(config));

        appender.start();
        return appender;
    }

    static Appender<ILoggingEvent> rollingFileAppender(AuditConfigurationMap config, LoggerContext loggerContext) {
        final FlexibleWindowRollingPolicy rollingPolicy = new FlexibleWindowRollingPolicy();
        rollingPolicy.setMaxBackup(APPENDER_FILE_MAXBACKUP.getInteger(config));
        rollingPolicy.setFileNamePattern(APPENDER_FILE_PATH.getString(config) + ".%i");
        rollingPolicy.setContext(loggerContext);

        final SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new SizeBasedTriggeringPolicy<>();
        triggeringPolicy.setMaxFileSize(new FileSize(APPENDER_FILE_MAXSIZE.getLong(config)));
        triggeringPolicy.setContext(loggerContext);

        final RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();

        appender.setName("auditFileAppender");
        appender.setContext(loggerContext);
        appender.setAppend(true);
        appender.setRollingPolicy(rollingPolicy);
        appender.setTriggeringPolicy(triggeringPolicy);
        appender.setEncoder(logbackEncoder(config, logstashLayout(config, loggerContext)));
        appender.setFile(APPENDER_FILE_PATH.getString(config));

        rollingPolicy.setParent(appender);

        rollingPolicy.start();
        triggeringPolicy.start();
        appender.start();

        return appender;
    }

    static Appender<ILoggingEvent> consoleAppender(AuditConfigurationMap config, LoggerContext loggerContext) {
        final LogTarget target = APPENDER_CONSOLE_TARGET.getValue(config, LogTarget.class);

        final PatternLayout layout = new PatternLayout();
        layout.setPattern(APPENDER_CONSOLE_PATTERN.getString(config));
        layout.setContext(loggerContext);
        layout.start();

        final ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();

        appender.setName("auditConsoleAppender");
        appender.setContext(loggerContext);
        appender.setTarget(target.getTarget());
        appender.setEncoder(logbackEncoder(config, layout));

        appender.start();

        return appender;
    }

    static Appender<ILoggingEvent> httpAppender(AuditConfigurationMap config, LoggerContext loggerContext) {
        final LogbackHttpAppender appender = new LogbackHttpAppender();

        appender.setName("auditHttpAppender");
        appender.setContext(loggerContext);
        appender.setLayout(logstashLayout(config, loggerContext));
        appender.setUrl(APPENDER_HTTP_URL.getString(config));
        if (!APPENDER_HTTP_USERNAME.getString(config).trim().isEmpty()) {
            appender.setUsername(APPENDER_HTTP_USERNAME.getString(config));
        }
        if (!APPENDER_HTTP_PASSWORD.getString(config).trim().isEmpty()) {
            appender.setPassword(APPENDER_HTTP_PASSWORD.getString(config));
        }
        appender.setAsync(APPENDER_HTTP_ASYNC.getBoolean(config));

        appender.setConnectTimeout(APPENDER_HTTP_CONNECT_TIMEOUT.getInteger(config));
        appender.setReadTimeout(APPENDER_HTTP_READ_TIMEOUT.getInteger(config));
        appender.setEncoding(ENCODING.getString(config));

        switch (PROPAGATE_APPENDER_EXCEPTIONS.getValue(config, PropagateExceptions.class)) {
        case ALL -> appender.setPropagateExceptions(true);
        case NONE -> appender.setPropagateExceptions(false);
        default -> throw new AuditLoggingException(
                "Unknown propagate exception value: " + PROPAGATE_APPENDER_EXCEPTIONS.getValue(config,
                        PropagateExceptions.class));
        }

        appender.start();

        return appender;
    }

    static Encoder<ILoggingEvent> logbackEncoder(AuditConfigurationMap config, Layout<ILoggingEvent> layout) {
        final LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setCharset(Charset.forName(ENCODING.getString(config)));
        encoder.setImmediateFlush(true);
        encoder.setLayout(layout);
        encoder.setContext(layout.getContext());

        encoder.start();

        return encoder;
    }

    static Layout<ILoggingEvent> logstashLayout(AuditConfigurationMap config, LoggerContext loggerContext) {
        Map<String, String> metaFields = new HashMap<>();
        metaFields.put(EventFields.MDC_ID, EventFields.ID);
        metaFields.put(EventFields.MDC_CATEGORY, EventFields.CATEGORY);
        metaFields.put(EventFields.MDC_AUDIT, EventFields.AUDIT);
        metaFields.put(EventFields.MDC_APPLICATION, EventFields.APPLICATION);
        metaFields.put(EventFields.MDC_SERVICE, EventFields.SERVICE);
        metaFields.put(EventFields.MDC_INSTANCE, EventFields.INSTANCE);

        LogbackJSONLayout layout = new LogbackJSONLayout();

        layout.setLocationInfo(LOCATION.getBoolean(config));
        layout.setHostInfo(HOST.getBoolean(config));
        layout.setMetaFields(metaFields);
        layout.setAddEventUuid(false);
        layout.setContext(loggerContext);

        // Ensure that non-ECS compliant fields preserved, see TDKN-319
        layout.setLegacyMode(true);

        layout.start();

        return layout;
    }
}
