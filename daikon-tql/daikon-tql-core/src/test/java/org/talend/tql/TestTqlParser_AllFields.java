// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.talend.tql.model.TqlElement;

public class TestTqlParser_AllFields extends TestTqlParser_Abstract {

    @Test
    public void testParseLiteralComparisonNeq() throws Exception {
        TqlElement tqlElement = doTest("*!=0");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=NEQ}, field=AllFields{}, valueOrField=LiteralValue{literal=INT, value='0'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonLt() throws Exception {
        TqlElement tqlElement = doTest("*<0");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=LT}, field=AllFields{}, valueOrField=LiteralValue{literal=INT, value='0'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonGt() throws Exception {
        TqlElement tqlElement = doTest("*>0");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=GT}, field=AllFields{}, valueOrField=LiteralValue{literal=INT, value='0'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonLet() throws Exception {
        TqlElement tqlElement = doTest("*<=0");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=LET}, field=AllFields{}, valueOrField=LiteralValue{literal=INT, value='0'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonGet() throws Exception {
        TqlElement tqlElement = doTest("*>=0");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[ComparisonExpression{operator=ComparisonOperator{operator=GET}, field=AllFields{}, valueOrField=LiteralValue{literal=INT, value='0'}}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonInvalid() throws Exception {
        TqlElement tqlElement = doTest("* is invalid");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[FieldIsInvalidExpression{field='AllFields{}'}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonValid() throws Exception {
        TqlElement tqlElement = doTest("* is valid");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[FieldIsValidExpression{field='AllFields{}'}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonComplies() throws Exception {
        TqlElement tqlElement = doTest("* complies ''");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[FieldCompliesPattern{field='AllFields{}', pattern=''}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonIsEmpty() throws Exception {
        TqlElement tqlElement = doTest("* is empty");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[FieldIsEmptyExpression{field='AllFields{}'}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonBetween() throws Exception {
        TqlElement tqlElement = doTest("* between [0, 5]");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[FieldBetweenExpression{field='AllFields{}', left=LiteralValue{literal=INT, value='0'}, right=LiteralValue{literal=INT, value='5'}, isLowerOpen=false, isUpperOpen=false}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonIn() throws Exception {
        TqlElement tqlElement = doTest("* in ['value1', 'value2']");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[FieldInExpression{field='AllFields{}', values=[LiteralValue{literal=QUOTED_VALUE, value='value1'}, LiteralValue{literal=QUOTED_VALUE, value='value2'}]}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonMatches() throws Exception {
        TqlElement tqlElement = doTest("* ~ '[a-z]+@daikon.[a-z]+'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[FieldMatchesRegex{field='AllFields{}', regex='[a-z]+@daikon.[a-z]+'}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonContains() throws Exception {
        TqlElement tqlElement = doTest("* contains 'value1'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[FieldContainsExpression{field='AllFields{}', value='value1', caseSensitive=true}]}]}";
        assertEquals(expected, tqlElement.toString());
    }

    @Test
    public void testParseLiteralComparisonContainsIgnoreCase() throws Exception {
        TqlElement tqlElement = doTest("* containsIgnoreCase 'value1'");
        String expected = "OrExpression{expressions=[AndExpression{expressions=[FieldContainsExpression{field='AllFields{}', value='value1', caseSensitive=false}]}]}";
        assertEquals(expected, tqlElement.toString());
    }
}
