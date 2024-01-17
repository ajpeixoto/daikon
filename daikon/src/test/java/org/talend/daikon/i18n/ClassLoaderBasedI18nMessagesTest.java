// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ClassLoaderBasedI18nMessagesTest {

    @Test
    public void testGetMessageWithSpecificUnknownKeyPrefix() {
        I18nMessages i18nMessages = new ClassLoaderBasedI18nMessages("org.talend.daikon.i18n.testMessage", "!"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("", i18nMessages.getMessage("ze.empty.key"));
        assertEquals("normal", i18nMessages.getMessage("ze.normal.key"));
        assertEquals("normal", i18nMessages.getMessage("ze.normal.key", "foo"));
        assertEquals("test {0} and {1}", i18nMessages.getMessage("ze.message.key"));
        assertEquals("test foo and bar", i18nMessages.getMessage("ze.message.key", "foo", "bar"));
        assertEquals("!unknown.key", i18nMessages.getMessage("unknown.key"));
    }

}
