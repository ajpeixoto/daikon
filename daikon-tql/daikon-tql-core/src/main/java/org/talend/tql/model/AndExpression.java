// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql.model;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.talend.tql.visitor.IASTVisitor;

/**
 * Logical conjunction of given set of Tql expressions.
 */

public class AndExpression implements Expression {

    private final Expression[] expressions;

    public AndExpression(Expression... expressions) {
        this.expressions = expressions;
    }

    public Expression[] getExpressions() {
        return expressions;
    }

    @Override
    public String toString() {
        return "AndExpression{" + "expressions=" + Arrays.toString(expressions) + '}';
    }

    @Override
    public String toQueryString() {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(expressions).limit(expressions.length - 1).map(expression -> "(" + expression.toQueryString() + ") and ")
                .forEach(sb::append);
        Arrays.stream(expressions).skip(expressions.length - 1).map(expression -> "(" + expression.toQueryString() + ")")
                .forEach(sb::append);

        return sb.toString();
    }

    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object expression) {
        return expression instanceof AndExpression
                && new EqualsBuilder().append(((AndExpression) expression).getExpressions(), this.expressions).isEquals();
    }
}
