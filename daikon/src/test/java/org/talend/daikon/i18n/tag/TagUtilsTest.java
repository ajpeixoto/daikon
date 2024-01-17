// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.talend.daikon.i18n.ClassBasedI18nMessages;
import org.talend.daikon.i18n.I18nMessages;

/**
 * Tests for {@link TagUtils} class.
 */
public class TagUtilsTest {

    private static I18nMessages i18nMessages;

    @BeforeAll
    public static void setUp() {
        i18nMessages = new ClassBasedI18nMessages(TagUtilsTest.class);
    }

    @Test
    public void testEmptyTag() {
        TagImpl parentTag = new TagImpl("parentTag");
        TagImpl childTag = new TagImpl("nonExistentTag", parentTag);
        parentTag.setI18nMessageFormatter(i18nMessages);
        childTag.setI18nMessageFormatter(i18nMessages);

        String translatedChildTagValue = TagUtils.getTranslatedPathToRoot(childTag);
        assertEquals("parent/nonExistentTag", translatedChildTagValue);
    }

    @Test
    public void testCommonTag() {
        TagImpl childTag = new TagImpl("childTag", CommonTestTags.COMMON_TAG);
        TagImpl childTag1 = new TagImpl("nonExistentTag", CommonTestTags.COMMON_TAG);
        CommonTestTags.COMMON_TAG.setI18nMessageFormatter(i18nMessages);
        childTag.setI18nMessageFormatter(i18nMessages);
        childTag1.setI18nMessageFormatter(i18nMessages);

        assertEquals("Common tag/child", TagUtils.getTranslatedPathToRoot(childTag));
        assertEquals("Common tag/nonExistentTag", TagUtils.getTranslatedPathToRoot(childTag1));
    }

    @Test
    public void testHasTranslatedTagValue() {
        TagImpl tag = new TagImpl("testTag");
        tag.setI18nMessageFormatter(i18nMessages);

        assertTrue(TagUtils.hasTag(tag, "Testing"));
        assertTrue(TagUtils.hasTag(tag, "sting tag"));
        assertTrue(TagUtils.hasTag(tag, "test"));
    }

    @Test
    public void testHasTagValue() {
        TagImpl parentTag = new TagImpl("parentTag");
        TagImpl tag = new TagImpl("testTag", parentTag);
        tag.setI18nMessageFormatter(i18nMessages);
        tag.setI18nMessageFormatter(i18nMessages);

        assertTrue(TagUtils.hasTag(tag, "testTag"));
        assertTrue(TagUtils.hasTag(tag, "parent"));
        assertTrue(TagUtils.hasTag(tag, "parentTag"));
    }

    @Test
    public void testHasParentTag() {
        TagImpl tag = new TagImpl("testTag", CommonTestTags.COMMON_TAG);
        tag.setI18nMessageFormatter(i18nMessages);

        assertTrue(TagUtils.hasTag(tag, "Common tag"));
        assertTrue(TagUtils.hasTag(tag, "Com"));
        assertTrue(TagUtils.hasTag(tag, "mon tag"));
        assertTrue(TagUtils.hasTag(tag, "Common tag/Testing"));
        assertTrue(TagUtils.hasTag(tag, "Testing"));
    }

    @Test
    public void testDoesntHaveTag() {
        TagImpl tag = new TagImpl("testTag", CommonTestTags.COMMON_TAG);
        tag.setI18nMessageFormatter(i18nMessages);

        assertFalse(TagUtils.hasTag(tag, "MySQL"));
        assertFalse(TagUtils.hasTag(tag, "Cloud"));
    }

    @Test
    public void testHierarchyTag() {
        TagImpl parentTag = new TagImpl("parentTag");
        TagImpl childTag = new TagImpl("childTag", parentTag);
        parentTag.setI18nMessageFormatter(i18nMessages);
        childTag.setI18nMessageFormatter(i18nMessages);

        String translatedParentTagValue = TagUtils.getTranslatedPathToRoot(parentTag);
        assertEquals("parent", translatedParentTagValue);
        String translatedChildTagValue = TagUtils.getTranslatedPathToRoot(childTag);
        assertEquals("parent/child", translatedChildTagValue);
    }

}
