// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.talend.daikon.i18n.package1.package2.TestClass2InheritingTestClass1;

public class ClassBasedI18nMessagesTest {

    @Test
    public void testGetMessageValueTopClass() {
        ClassBasedI18nMessages classBasedI18nMessages = new ClassBasedI18nMessages(TestClass2InheritingTestClass1.class);
        assertEquals("package2.a.key.value", classBasedI18nMessages.getMessage("a.key"));
        assertEquals("package2.another.key.value", classBasedI18nMessages.getMessage("another.key"));
    }

    @Test
    public void testGetMessageValueInheritedClass() {
        ClassBasedI18nMessages classBasedI18nMessages = new ClassBasedI18nMessages(TestClass2InheritingTestClass1.class);
        assertEquals("package1.unique.key.value", classBasedI18nMessages.getMessage("unique.key"));
    }

    @Test
    public void testGetMessageValueClassNameMessageProperties() {
        ClassBasedI18nMessages classBasedI18nMessages = new ClassBasedI18nMessages(this.getClass());
        // check the value from the classname message property
        assertEquals("the good value for a key", classBasedI18nMessages.getMessage("a.key"));
        // check the value from the package message property
        assertEquals("value for the second key", classBasedI18nMessages.getMessage("a.second.key"));
    }

}
