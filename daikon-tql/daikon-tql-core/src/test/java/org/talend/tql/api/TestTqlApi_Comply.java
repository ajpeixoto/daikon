// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.talend.tql.api.TqlBuilder.complies;

import org.junit.jupiter.api.Test;
import org.talend.tql.TestTqlParser_Abstract;
import org.talend.tql.model.TqlElement;

public class TestTqlApi_Comply extends TestTqlParser_Abstract {

    @Test
    public void testApiFieldCompliesPattern1() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies 'aaaaaaa'");
        // TQL api query
        TqlElement tqlElement = complies("name", "aaaaaaa");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldCompliesPattern2() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies 'Aaaaaaa'");
        // TQL api query
        TqlElement tqlElement = complies("name", "Aaaaaaa");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldCompliesPattern3() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies 'Aaaaaa 9aaa'");
        // TQL api query
        TqlElement tqlElement = complies("name", "Aaaaaa 9aaa");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldCompliesPattern4() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies 'Aaa Aaaa'");
        // TQL api query
        TqlElement tqlElement = complies("name", "Aaa Aaaa");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldCompliesPattern5() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies 'Aaaa_99'");
        // TQL api query
        TqlElement tqlElement = complies("name", "Aaaa_99");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldCompliesPattern6() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies ']ss@'");
        // TQL api query
        TqlElement tqlElement = complies("name", "]ss@");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldCompliesPattern7() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies 'Aaaa أبجد Aaaa'");
        // TQL api query
        TqlElement tqlElement = complies("name", "Aaaa أبجد Aaaa");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldCompliesPattern8() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies ''");
        // TQL api query
        TqlElement tqlElement = complies("name", "");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldCompliesPattern9() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies '\\''");
        // TQL api query
        TqlElement tqlElement = complies("name", "'");
        assertEquals(expected.toString(), tqlElement.toString());
    }

    @Test
    public void testApiFieldCompliesPattern10() throws Exception {
        // TQL native query
        TqlElement expected = doTest("name complies 'C\\'est quoi'");
        // TQL api query
        TqlElement tqlElement = complies("name", "C'est quoi");
        assertEquals(expected.toString(), tqlElement.toString());
    }
}
