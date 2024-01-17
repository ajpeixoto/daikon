// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.talend.daikon.i18n.ClassBasedI18nMessages;
import org.talend.daikon.i18n.I18nMessages;

/**
 * Tests for {@link TagImpl} class
 */
public class TagImplTest {

    private static I18nMessages i18nMessages;

    @BeforeAll
    public static void setUp() {
        i18nMessages = new ClassBasedI18nMessages(TagImplTest.class);
    }

    @Test
    public void testTagMessage() {
        TagImpl tag = new TagImpl("testTag");
        tag.setI18nMessageFormatter(i18nMessages);

        assertEquals("Testing tag", tag.getTranslatedValue());
        assertEquals("testTag", tag.getValue());
        assertNull(tag.getParentTag());
    }

    @Test
    public void testWithParentTag() {
        TagImpl parentTag = new TagImpl("parentTag");
        TagImpl tag = new TagImpl("testTag", parentTag);
        parentTag.setI18nMessageFormatter(i18nMessages);
        tag.setI18nMessageFormatter(i18nMessages);

        assertEquals("Testing tag", tag.getTranslatedValue());
        assertEquals("testTag", tag.getValue());
        assertNotNull(tag.getParentTag());
        Tag retrievedParentTag = tag.getParentTag();
        assertEquals("parentTag", retrievedParentTag.getValue());
        assertEquals("parent", retrievedParentTag.getTranslatedValue());
        assertNull(retrievedParentTag.getParentTag());
    }

    @Test
    public void testEmptyTag() {
        TagImpl tag = new TagImpl("nonExistentTag", null);
        tag.setI18nMessageFormatter(i18nMessages);
        assertEquals("nonExistentTag", tag.getTranslatedValue());
    }

}
