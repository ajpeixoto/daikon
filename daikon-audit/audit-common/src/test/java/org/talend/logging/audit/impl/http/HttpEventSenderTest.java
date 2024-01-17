// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.logging.audit.impl.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

@SetSystemProperty(key = "org.talend.logging.audit.impl.http.HttpEventSender.username", value = "system-prop-user")
@SetSystemProperty(key = "org.talend.logging.audit.impl.http.HttpEventSender.encoding", value = "UTF-16")
@SetSystemProperty(key = "org.talend.logging.audit.impl.http.HttpEventSender.connectTimeout", value = "5000")
public class HttpEventSenderTest {

    @Test
    public void overrideSystemProps() {
        final HttpEventSender sender = new HttpEventSender();
        try {
            sender.start();
        } catch (final HttpAppenderException ex) {
            // not important, we went through overriding anyway
        }
        assertEquals("system-prop-user", sender.getUsername());
        assertEquals(5000, sender.getConnectTimeout());
        assertEquals(StandardCharsets.UTF_16, sender.getEncoding());
    }
}
