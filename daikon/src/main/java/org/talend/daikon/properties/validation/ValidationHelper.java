// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.validation;

import org.talend.daikon.properties.AnyPropertyVisitor;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.ValidationResults;
import org.talend.daikon.properties.property.Property;

public class ValidationHelper {

    /**
     * Validates component properties and its subproperties and returns results of validation process.
     * Each Property field can be validated either separately, using {@link Property#validate()} method or {@link Validator} set
     * to the Property field, or using {@link Properties#validate()} method, which can be used to validate complex Properties in
     * one method.
     *
     * @return {@link ValidationResults} for the component properties
     */
    public static ValidationResults validateProperties(Properties props) {
        final ValidationResults results = new ValidationResults();
        props.accept(new AnyPropertyVisitor() {

            @Override
            public void visit(Properties properties, Properties parent) {
                results.addValidationResults(properties.validate());
            }

            @Override
            public void visit(Property property, Properties parent) {
                results.addValidationResult(property.getName(), property.validate());
            }
        }, null);
        return results;
    }

}
