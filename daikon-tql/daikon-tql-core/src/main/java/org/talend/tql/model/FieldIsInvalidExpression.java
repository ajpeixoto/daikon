// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.tql.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.talend.tql.visitor.IASTVisitor;

/**
 * Created by gmzoughi on 02/09/16.
 */
public class FieldIsInvalidExpression implements Atom {

    private final TqlElement field;

    public FieldIsInvalidExpression(TqlElement field) {
        this.field = field;
    }

    public TqlElement getField() {
        return field;
    }

    @Override
    public String toString() {
        return "FieldIsInvalidExpression{" + "field='" + field + '\'' + '}';
    }

    @Override
    public String toQueryString() {
        return field.toQueryString() + " is invalid";
    }

    @Override
    public <T> T accept(IASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object expression) {
        return expression instanceof FieldIsInvalidExpression
                && new EqualsBuilder().append(field, ((FieldIsInvalidExpression) expression).field).isEquals();
    }
}
