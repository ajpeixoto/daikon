// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.talend.tql.model.TqlElement;

public class TestTqlParser_Boolean extends TestTqlParser_Abstract {

    @Test
    public void testParseLiteralComparisonEqTrue() throws Exception {
        TqlElement tqlElement = doTest("field1=true");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=EQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=BOOLEAN, value='true'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonNeqTrue() throws Exception {
        TqlElement tqlElement = doTest("field1!=true");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=NEQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=BOOLEAN, value='true'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonEqFalse() throws Exception {
        TqlElement tqlElement = doTest("field1=false");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=EQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=BOOLEAN, value='false'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonNeqFalse() throws Exception {
        TqlElement tqlElement = doTest("field1!=false");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=NEQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=BOOLEAN, value='false'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

}
