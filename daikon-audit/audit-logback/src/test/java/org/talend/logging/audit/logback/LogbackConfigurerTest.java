// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.logging.audit.logback;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.talend.logging.audit.logback.LogbackConfigurer.LOG_AUDIT_SOCKET_APPENDER_NAME;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.talend.daikon.logging.event.layout.LogbackJSONLayout;
import org.talend.logging.audit.impl.AuditConfiguration;
import org.talend.logging.audit.impl.AuditConfigurationMap;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEventVO;
import ch.qos.logback.classic.util.LogbackMDCAdapter;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.net.AbstractSocketAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;

public class LogbackConfigurerTest {

    private static final String HTTP_APPENDER = "auditHttpAppender";

    private static final String FILE_APPENDER = "auditFileAppender";

    private static final String CONSOLE_APPENDER = "auditConsoleAppender";

    @Test
    public void testConfigurer() {
        final LoggerContext context = new LoggerContext();
        final AuditConfigurationMap config = AuditConfiguration.loadFromClasspath("/configurer.audit.properties");

        LogbackConfigurer.configure(config, context);

        final Logger logger = context.getLogger("testLogger");

        validateHttpAppender((LogbackHttpAppender) logger.getAppender(HTTP_APPENDER));
        validateFileAppender((RollingFileAppender<ILoggingEvent>) logger.getAppender(FILE_APPENDER));
        validateConsoleAppender((ConsoleAppender<ILoggingEvent>) logger.getAppender(CONSOLE_APPENDER));
    }

    @Test
    public void testSocketConfig() throws IOException, InterruptedException {
        final Collection<String> messages = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);
        try (final ServerSocket socket = new ServerSocket(0)) {
            System.setProperty("LogbackConfigurerTest_testSocketConfig_port", Integer.toString(socket.getLocalPort()));

            Thread threadLogListener = new Thread(() -> {
                try (final Socket accept = socket.accept();
                        final ObjectInputStream stream = new ObjectInputStream(accept.getInputStream())) {

                    final LoggingEventVO eventVO = (LoggingEventVO) stream.readObject();
                    synchronized (messages) {
                        messages.add(eventVO.getMessage() + "/" + eventVO.getMdc().get("application"));
                    }
                } catch (final IOException | ClassNotFoundException e) {
                    fail(e.getMessage());
                } finally {
                    latch.countDown();
                }
            }, getClass() + ".testSocketConfig");
            threadLogListener.start();

            final LoggerContext loggerContext = new LoggerContext();
            if (loggerContext.getMDCAdapter() == null) {
                // Cf https://github.com/spring-projects/spring-boot/issues/36177
                loggerContext.setMDCAdapter((LogbackMDCAdapter) MDC.getMDCAdapter());
            }
            final AuditConfigurationMap config = AuditConfiguration.loadFromClasspath("/configurer.socket.audit.properties");
            LogbackConfigurer.configure(config, loggerContext);
            final Logger logger = loggerContext.getLogger("testSocketLogger");

            final Appender<ILoggingEvent> appender = logger.getAppender(LOG_AUDIT_SOCKET_APPENDER_NAME);
            assertNotNull(appender);
            assertThat(appender, instanceOf(AbstractSocketAppender.class));

            final AbstractSocketAppender socketAppender = (AbstractSocketAppender) appender;

            assertEquals("localhost", socketAppender.getRemoteHost());
            assertEquals(socket.getLocalPort(), socketAppender.getPort());
            assertTrue(socketAppender.isStarted());

            final String json = "{\"message\":\"yes\"}";
            logger.info(json);
            latch.await();
            synchronized (messages) {
                assertEquals(1, messages.size());
                assertEquals(json + "/appName", messages.iterator().next());
                messages.clear();
            }
        } finally {
            System.clearProperty("LogbackConfigurerTest_testSocketConfig_port");
        }
    }

    private static void validateConsoleAppender(ConsoleAppender<ILoggingEvent> appender) {
        assertNotNull(appender);

        assertEquals("System.err", appender.getTarget());

        PatternLayout layout = getLayout(appender.getEncoder(), PatternLayout.class);

        assertEquals("ConsolePattern", layout.getPattern());
    }

    private static void validateFileAppender(RollingFileAppender<ILoggingEvent> appender) {
        assertNotNull(appender);

        final FlexibleWindowRollingPolicy rollingPolicy = (FlexibleWindowRollingPolicy) appender.getRollingPolicy();

        assertEquals("/tmp/test.json", appender.getFile());
        assertEquals(100, rollingPolicy.getMaxIndex());
        assertTrue(appender.getTriggeringPolicy() instanceof SizeBasedTriggeringPolicy);
        assertNotNull(getLayout(appender.getEncoder(), LogbackJSONLayout.class));
    }

    private static void validateHttpAppender(LogbackHttpAppender appender) {
        assertNotNull(appender);

        assertEquals("http://localhost:8080/", appender.getUrl());
        assertEquals("httpuser", appender.getUsername());
        assertEquals(1000, appender.getConnectTimeout());
        assertEquals(50, appender.getReadTimeout());
        assertFalse(appender.isAsync());
        assertTrue(appender.isPropagateExceptions());
        assertEquals("UTF-16", appender.getEncoding());

        assertTrue(appender.getLayout() instanceof LogbackJSONLayout);
        assertTrue(((LogbackJSONLayout) appender.getLayout()).isLegacyMode());
    }

    private static <T extends Layout<ILoggingEvent>> T getLayout(Encoder<ILoggingEvent> encoder, Class<T> clz) {
        return clz.cast(((LayoutWrappingEncoder<ILoggingEvent>) encoder).getLayout());
    }
}
