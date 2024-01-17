// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.talend.tql.api.TqlBuilder.cloneExpression;

import org.junit.jupiter.api.Test;
import org.talend.tql.TestTqlParser_Abstract;
import org.talend.tql.model.Expression;
import org.talend.tql.model.OrExpression;
import org.talend.tql.model.TqlElement;

/**
 * Created by achever on 04/07/17.
 */
public class TestTqlApi_Clone extends TestTqlParser_Abstract {

    @Test
    public void testApiClone1() throws Exception {
        // Expected TQL
        Expression toClone = (Expression) doTest("(f1=1 and f2=2) or f3=true");
        // TQL
        TqlElement cloned = cloneExpression(toClone);
        assertNotSame(toClone, cloned);
        assertNotSame(((OrExpression) toClone).getExpressions(), ((OrExpression) cloned).getExpressions());
        assertEquals(toClone.toString(), cloned.toString());
    }

    @Test
    public void testApiClone2() throws Exception {
        // Expected TQL
        Expression toClone = (Expression) doTest("(f1=1 and (not(f2=2 and f4=4))) or f3=true");
        // TQL
        TqlElement cloned = cloneExpression(toClone);
        assertNotSame(toClone, cloned);
        assertNotSame(((OrExpression) toClone).getExpressions(), ((OrExpression) cloned).getExpressions());
        assertEquals(toClone.toString(), cloned.toString());
    }

    @Test
    public void testApiClone3() throws Exception {
        // Expected TQL
        Expression toClone = (Expression) doTest(
                "((f1=1 and (f2=2 or f2=22)) or f3=3) and not (f3 between [1.2, 9.45] or (f4 is valid and f5 is empty))");
        // TQL
        TqlElement cloned = cloneExpression(toClone);
        assertNotSame(toClone, cloned);
        assertNotSame(((OrExpression) toClone).getExpressions(), ((OrExpression) cloned).getExpressions());
        assertEquals(toClone.toString(), cloned.toString());
    }

}
