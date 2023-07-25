package org.talend.logging.audit.log4j2;

import static org.apache.logging.log4j.Level.OFF;
import static org.apache.logging.log4j.core.appender.ConsoleAppender.Target.SYSTEM_ERR;
import static org.apache.logging.log4j.core.appender.ConsoleAppender.Target.SYSTEM_OUT;
import static org.talend.logging.audit.LogAppenders.NONE;
import static org.talend.logging.audit.impl.AuditConfiguration.*;
import static org.talend.logging.audit.impl.EventFields.*;
import static org.talend.logging.audit.impl.LogTarget.ERROR;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.ConsoleAppender.Target;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.talend.daikon.logging.event.layout.Log4j2JSONLayout;
import org.talend.logging.audit.AuditLoggingException;
import org.talend.logging.audit.LogAppenders;
import org.talend.logging.audit.impl.*;

/**
 *
 */
public final class Log4j2Configurer {

    public static final Map<String, String> META_FIELDS = new HashMap<>();

    static {
        META_FIELDS.put(MDC_ID, ID);
        META_FIELDS.put(MDC_CATEGORY, CATEGORY);
        META_FIELDS.put(MDC_AUDIT, AUDIT);
        META_FIELDS.put(MDC_APPLICATION, APPLICATION);
        META_FIELDS.put(MDC_SERVICE, SERVICE);
        META_FIELDS.put(MDC_INSTANCE, INSTANCE);
    }

    private Log4j2Configurer() {
    }

    public static void configure(AuditConfigurationMap config) {
        final LogAppendersSet appendersSet = LOG_APPENDER.getValue(config, LogAppendersSet.class);

        if (appendersSet == null || appendersSet.isEmpty()) {
            throw new AuditLoggingException("No audit appenders configured.");
        }

        if (appendersSet.size() > 1 && appendersSet.contains(NONE)) {
            throw new AuditLoggingException("Invalid configuration: none appender is used with other simultaneously.");
        }

        String root_logger_name = ROOT_LOGGER.getString(config);
        final Logger logger = LogManager.getLogger(root_logger_name);

        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = loggerContext.getConfiguration();
        LoggerConfig loggerConfig = configuration.getLoggerConfig(root_logger_name);
        loggerConfig.setAdditive(false);

        for (LogAppenders appender : appendersSet) {
            switch (appender) {
            case FILE -> {
                Appender fa = rollingFileAppender(config);
                configuration.addAppender(fa);
                loggerConfig.addAppender(fa, logger.getLevel(), null);
                configuration.addLogger(root_logger_name, loggerConfig);
                loggerContext.updateLoggers();
            }
            case SOCKET -> {
                Appender sa = socketAppender(config);
                configuration.addAppender(sa);
                loggerConfig.addAppender(sa, logger.getLevel(), null);
                configuration.addLogger(root_logger_name, loggerConfig);
                loggerContext.updateLoggers();
            }
            case CONSOLE -> {
                Appender ca = consoleAppender(config);
                configuration.addAppender(ca);
                loggerConfig.addAppender(ca, logger.getLevel(), null);
                configuration.addLogger(root_logger_name, loggerConfig);
                loggerContext.updateLoggers();
            }
            case HTTP -> {
                Appender ha = httpAppender(config);
                configuration.addAppender(ha);
                loggerConfig.addAppender(ha, logger.getLevel(), null);
                configuration.addLogger(root_logger_name, loggerConfig);
                loggerContext.updateLoggers();
            }
            case NONE -> {
                loggerConfig.setLevel(OFF);
                loggerContext.updateLoggers();
            }
            default -> throw new AuditLoggingException("Unknown appender " + appender);
            }
        }
    }

    private static Appender rollingFileAppender(AuditConfigurationMap config) {
        DefaultRolloverStrategy strategy = DefaultRolloverStrategy.newBuilder()
                .withMax(String.valueOf(AuditConfiguration.APPENDER_FILE_MAXBACKUP.getInteger(config))).build();
        SizeBasedTriggeringPolicy policy = SizeBasedTriggeringPolicy
                .createPolicy(String.valueOf(AuditConfiguration.APPENDER_FILE_MAXSIZE.getLong(config)));

        String logfilepath = AuditConfiguration.APPENDER_FILE_PATH.getString(config);
        String parent_dir = "";
        int index = logfilepath.lastIndexOf('/');
        if (index > 0) {
            parent_dir = logfilepath.substring(0, index + 1);
        }

        String archived_log_file_pattern = parent_dir + "logs/audit-%d{yyyy-MM-dd}-%i.log.gz";

        Appender appender = RollingFileAppender.newBuilder().setName("auditFileAppender").withStrategy(strategy)
                .withPolicy(policy).setImmediateFlush(true).withAppend(true).setBufferedIo(false).setBufferSize(8 * 1024)
                .withFileName(logfilepath).withFilePattern(archived_log_file_pattern).setLayout(logstashLayout(config)).build();

        appender.start();
        return appender;
    }

    private static Appender socketAppender(AuditConfigurationMap config) {
        Appender appender = SocketAppender.newBuilder().setName("auditSocketAppender")
                .setHost(APPENDER_SOCKET_HOST.getString(config)).setPort(APPENDER_SOCKET_PORT.getInteger(config))
                .setLayout(logstashLayout(config)).build();
        appender.start();
        return appender;
    }

    private static Appender consoleAppender(AuditConfigurationMap config) {
        final LogTarget target = APPENDER_CONSOLE_TARGET.getValue(config, LogTarget.class);

        Target tg = SYSTEM_OUT;
        if (target == ERROR) {
            tg = SYSTEM_ERR;
        }
        Appender appender = ConsoleAppender.newBuilder().setName("auditConsoleAppender").setTarget(tg)
                .setLayout(PatternLayout.newBuilder().withPattern(APPENDER_CONSOLE_PATTERN.getString(config)).build()).build();
        appender.start();
        return appender;
    }

    private static Appender httpAppender(AuditConfigurationMap config) {
        boolean ignoreExceptions = false;
        switch (PROPAGATE_APPENDER_EXCEPTIONS.getValue(config, PropagateExceptions.class)) {
        case ALL:
            break;
        case NONE:
            ignoreExceptions = true;
            break;
        default:
            throw new AuditLoggingException("Unknown propagate exception value: "
                    + PROPAGATE_APPENDER_EXCEPTIONS.getValue(config, PropagateExceptions.class));
        }
        final Log4j2HttpAppender appender = new Log4j2HttpAppender("auditHttpAppender", null, logstashLayout(config),
                ignoreExceptions, null);
        appender.setUrl(APPENDER_HTTP_URL.getString(config));
        if (!APPENDER_HTTP_USERNAME.getString(config).trim().isEmpty()) {
            appender.setUsername(APPENDER_HTTP_USERNAME.getString(config));
        }
        String trimmed = APPENDER_HTTP_PASSWORD.getString(config).trim();
        if (!trimmed.isEmpty()) {
            appender.setPassword(trimmed);
        }
        appender.setAsync(APPENDER_HTTP_ASYNC.getBoolean(config));

        appender.setConnectTimeout(APPENDER_HTTP_CONNECT_TIMEOUT.getInteger(config));
        appender.setReadTimeout(APPENDER_HTTP_READ_TIMEOUT.getInteger(config));
        appender.setEncoding(ENCODING.getString(config));

        appender.start();

        return appender;
    }

    private static Layout logstashLayout(AuditConfigurationMap config) {
        Log4j2JSONLayout layout = Log4j2JSONLayout.newBuilder().setLocationInfo(LOCATION.getBoolean(config))
                .setHostInfo(HOST.getBoolean(config)).build();

        layout.setMetaFields(META_FIELDS);

        return layout;
    }
}
