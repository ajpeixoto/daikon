// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.talend.tql.visitor.IASTVisitor;

/*
 * Tql comparison operators.
 */

/**
 * Created by gmzoughi on 23/06/16.
 */
public class ComparisonOperator implements TqlElement {

    private final Enum operator;

    public ComparisonOperator(Enum operator) {
        this.operator = operator;
    }

    public Enum getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "ComparisonOperator{" + "operator=" + operator + '}';
    }

    @Override
    public String toQueryString() {
        switch (operator) {
        case EQ:
            return "=";
        case LT:
            return "<";
        case GT:
            return ">";
        case NEQ:
            return "!=";
        case LET:
            return "<=";
        case GET:
            return ">=";
        case NOT:
            return "!";
        default:
            throw new RuntimeException("Unknown comparison operator");
        }
    }

    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object expression) {
        return expression instanceof ComparisonOperator
                && new EqualsBuilder().append(this.getOperator(), ((ComparisonOperator) expression).getOperator()).isEquals();
    }

    public enum Enum {
        EQ,
        LT,
        GT,
        NEQ,
        LET,
        GET,
        NOT
    }
}
