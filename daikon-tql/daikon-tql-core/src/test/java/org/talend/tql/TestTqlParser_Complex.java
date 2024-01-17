// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.talend.tql.excp.TqlException;
import org.talend.tql.model.TqlElement;

public class TestTqlParser_Complex extends TestTqlParser_Abstract {

    @Test
    public void testParseLiteralComparison() throws Exception {
        TqlElement tqlElement = doTest("field1='value1'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=EQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value1'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonWithParenthesis() throws Exception {
        TqlElement tqlElement = doTest("(((field1='value1')))");
        String expected = "OrExpression{expressions=[AndExpression{expressions=["
                + "OrExpression{expressions=[AndExpression{expressions=[OrExpression{expressions=[AndExpression{expressions=[OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{"
                + "operator=ComparisonOperator{operator=EQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value1'}}]}]}]}]}]}]}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseFieldComparison() throws Exception {
        TqlElement tqlElement = doTest("field1=field(field2)");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[" + "ComparisonExpression{"
                + "operator=ComparisonOperator{operator=EQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=FieldReference{path='field2'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseFieldIsEmpty() throws Exception {
        TqlElement tqlElement = doTest("field1 is empty");
        String expected = "OrExpression{expressions=[AndExpression{expressions=["
                + "FieldIsEmptyExpression{field='FieldReference{path='field1'}'}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseFieldIsValid() throws Exception {
        TqlElement tqlElement = doTest("field1 is valid");
        String expected = "OrExpression{expressions=[AndExpression{expressions=["
                + "FieldIsValidExpression{field='FieldReference{path='field1'}'}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseFieldIsInvalid() throws Exception {
        TqlElement tqlElement = doTest("field1 is invalid");
        String expected = "OrExpression{expressions=[AndExpression{expressions=["
                + "FieldIsInvalidExpression{field='FieldReference{path='field1'}'}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseFieldCompliesPattern() throws Exception {
        TqlElement tqlElement = doTest("field1 complies 'value1'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=["
                + "FieldCompliesPattern{field='FieldReference{path='field1'}', pattern='value1'}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseFieldCompliesPatternWrongPattern() {
        assertThrows(TqlException.class, () -> {
            doTest("field1 complies 123");
        });
    }

    @Test
    public void testParseFieldMatchesRegex() throws Exception {
        TqlElement tqlElement = doTest("field1 ~ 'value1'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=["
                + "FieldMatchesRegex{field='FieldReference{path='field1'}', regex='value1'}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseFieldMatchesRegexWrongRegex() {
        assertThrows(TqlException.class, () -> {
            doTest("field1 ~ 123");
        });
    }

    @Test
    public void testParseFieldContainsValue() throws Exception {
        TqlElement tqlElement = doTest("field1 contains 'value1'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=["
                + "FieldContainsExpression{field='FieldReference{path='field1'}', value='value1', caseSensitive=true}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseFieldContainsIgnoreCaseValue() throws Exception {
        TqlElement tqlElement = doTest("field1 containsIgnoreCase 'value1'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=["
                + "FieldContainsExpression{field='FieldReference{path='field1'}', value='value1', caseSensitive=false}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseFieldContainsValueWrongValue() {
        assertThrows(TqlException.class, () -> {
            doTest("field1 contains 123");
        });
    }

    @Test
    public void testParseNotExpression() throws Exception {
        TqlElement tqlElement = doTest("not (field1='value1')");
        String expected = "OrExpression{expressions=[AndExpression{expressions=["
                + "NotExpression{expression=OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{"
                + "operator=ComparisonOperator{operator=EQ}, " + "field=FieldReference{path='field1'}, "
                + "valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value1'}}]}]}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseAndExpressions() throws Exception {
        TqlElement tqlElement = doTest("field1='value1' and field2='value2' and field3='value3'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field1'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value1'}}"
                + ", ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field2'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value2'}}"
                + ", ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field3'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value3'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseComplexExpressions() throws Exception {
        TqlElement tqlElement = doTest("field1='value1' and field2 is empty or (not (field3='value3'))");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field1'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value1'}}, FieldIsEmptyExpression{field='FieldReference{path='field2'}'}]}, AndExpression{expressions=[OrExpression{expressions=[AndExpression{expressions=[NotExpression{expression=OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field3'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value3'}}]}]}}]}]}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseAndExpressionsParenthesis1() throws Exception {
        TqlElement tqlElement = doTest("(field1='value1' and field2='value2') or field3='value3'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field1'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value1'}}"
                + ", ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field2'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value2'}}]}]}]}"
                + ", AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field3'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value3'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseAndExpressionsParenthesis2() throws Exception {
        TqlElement tqlElement = doTest("field1='value1' and (field2='value2' or field3='value3')");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field1'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value1'}}"
                + ", OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field2'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value2'}}]}"
                + ", AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field3'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value3'}}]}]}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseComplexExpressionsNoParenthesis() throws Exception {
        TqlElement tqlElement = doTest("field1='value1' or field2='value2' and field3='value3'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field1'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value1'}}]}"
                + ", AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field2'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value2'}}"
                + ", ComparisonExpression{operator=ComparisonOperator{operator=EQ}, field=FieldReference{path='field3'}, valueOrField=LiteralValue{literal=QUOTED_VALUE, value='value3'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }
}
