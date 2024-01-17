// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.validation;

import org.talend.daikon.properties.ValidationResult;

/**
 * Interface representing validators for single property field. Validator can be set to instance of {@link Property} class, and it
 * will be used to validate value set to this Property object. Generic should be of the same class as Property generic type.
 */
public interface Validator<T> {

    /**
     * Method to validate Property value. This method should validate Property value and return {@link ValidationResult}.
     * 
     * @param value set to the Property field
     * @return ValidationResult
     */
    public ValidationResult validate(T value);

}
