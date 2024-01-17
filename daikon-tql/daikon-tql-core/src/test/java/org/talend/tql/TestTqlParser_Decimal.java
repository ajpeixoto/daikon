// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.talend.tql.model.TqlElement;

public class TestTqlParser_Decimal extends TestTqlParser_Abstract {

    @Test
    public void testParseLiteralComparisonEq() throws Exception {
        TqlElement tqlElement = doTest("field1=123.456");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=EQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=DECIMAL, value='123.456'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonNeq() throws Exception {
        TqlElement tqlElement = doTest("field1!=123.456");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=NEQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=DECIMAL, value='123.456'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonLt() throws Exception {
        TqlElement tqlElement = doTest("field1<123.456");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=LT}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=DECIMAL, value='123.456'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonGt() throws Exception {
        TqlElement tqlElement = doTest("field1>123.456");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=GT}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=DECIMAL, value='123.456'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonLet() throws Exception {
        TqlElement tqlElement = doTest("field1<=123.456");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=LET}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=DECIMAL, value='123.456'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonGet() throws Exception {
        TqlElement tqlElement = doTest("field1>=123.456");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=GET}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=DECIMAL, value='123.456'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonNegative() throws Exception {
        TqlElement tqlElement = doTest("field1=-123.456");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=EQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=DECIMAL, value='-123.456'}}]}]}";
        assertEquals(expected, tqlElement.toString());

        tqlElement = doTest("field1>=-123.456");
        expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=GET}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=DECIMAL, value='-123.456'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }
}
