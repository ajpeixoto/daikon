// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.talend.tql.api.TqlBuilder.between;

import org.junit.jupiter.api.Test;
import org.talend.tql.TestTqlParser_Abstract;
import org.talend.tql.model.TqlElement;

/**
 * Created by achever on 30/06/17.
 */
public class TestTqlApi_Between extends TestTqlParser_Abstract {

    @Test
    public void testApiFieldBetweenQuoted() throws Exception {
        // TQL native query
        TqlElement expected = doTest("field1 between ['value1', 'value2']");
        // TQL api query
        TqlElement tqlElement = between("field1", "value1", "value2");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldBetweenInt() throws Exception {
        // TQL native query
        TqlElement expected = doTest("field1 between [123, 456]");
        // TQL api query
        TqlElement tqlElement = between("field1", 123, 456);
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldBetweenDecimal() throws Exception {
        // TQL native query
        TqlElement expected = doTest("field1 between [123.45, 456.78]");
        // TQL api query
        TqlElement tqlElement = between("field1", 123.45, 456.78);
        assertEquals(expected.toString(), tqlElement.toString());
    }

}
