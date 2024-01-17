// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.strings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ToStringIndentUtilTest {

    @Test
    public void test() {
        assertEquals("", ToStringIndentUtil.indentString(-1));
        assertEquals("", ToStringIndentUtil.indentString(0));
        assertEquals(" ", ToStringIndentUtil.indentString(1));
        assertEquals("  ", ToStringIndentUtil.indentString(2));
        assertEquals("    ", ToStringIndentUtil.indentString(4));
        assertEquals("          ", ToStringIndentUtil.indentString(10));
    }

}
