// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.definition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class I18nDefinitionTest {

    @Test
    public void testGetTitle() {

        // check getTitle with proper i18n
        I18nDefinition i18nDefinition = getMockI18nDef();
        when(i18nDefinition.getI18nMessage("definition.foo.title")).thenReturn("ZeTitle");
        assertEquals("ZeTitle", i18nDefinition.getTitle());

        // check getTitle with no i18n but one available for displayname
        i18nDefinition = getMockI18nDef();
        when(i18nDefinition.getI18nMessage("definition.foo.displayName")).thenReturn("ZedisplayName");
        assertEquals("ZedisplayName", i18nDefinition.getTitle());

        // check getTitle with no i18n and no i18n for display name
        i18nDefinition = getMockI18nDef();
        assertEquals("definition.foo.title", i18nDefinition.getTitle());
    }

    private I18nDefinition getMockI18nDef() {
        I18nDefinition i18nDefinition = spy(new I18nDefinition(""));
        when(i18nDefinition.getName()).thenReturn("foo");
        return i18nDefinition;
    }

}
