// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.talend.tql.api.TqlBuilder.containsIgnoreCase;

import org.junit.jupiter.api.Test;
import org.talend.tql.TestTqlParser_Abstract;
import org.talend.tql.model.TqlElement;

public class TestTqlApi_ContainsIgnoreCase extends TestTqlParser_Abstract {

    @Test
    public void testApiFieldContainsIgnoreCase1() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name containsIgnoreCase 'ssen'");
        // TQL api query
        TqlElement tqlElement = containsIgnoreCase("name", "ssen");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldContainsIgnoreCase2() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name containsIgnoreCase 'noi'");
        // TQL api query
        TqlElement tqlElement = containsIgnoreCase("name", "noi");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldContainsIgnoreCase3() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name containsIgnoreCase '2'");
        // TQL api query
        TqlElement tqlElement = containsIgnoreCase("name", "2");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldContainsIgnoreCase4() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name containsIgnoreCase 'azerty'");
        // TQL api query
        TqlElement tqlElement = containsIgnoreCase("name", "azerty");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldContainsIgnoreCase5() throws Exception {
        TqlElement expected = doTest("name containsIgnoreCase ''");
        // TQL api query
        TqlElement tqlElement = containsIgnoreCase("name", "");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldContainsIgnoreCase6() throws Exception {
        TqlElement expected = doTest("name containsIgnoreCase 'aze\\'rty'");
        // TQL api query
        TqlElement tqlElement = containsIgnoreCase("name", "aze'rty");
        assertEquals(expected.toString(), tqlElement.toString());
    }

}
