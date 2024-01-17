// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.properties.property;

/**
 * Implement this to translate the stored value associated with a {@link Property} object into an actual value for the property.
 * When this is implemented, the stored value is typically some kind of key which can be looked up in a context to provide the
 * actual value.
 */
public interface PropertyValueEvaluator {

    /**
     * Compute the actual value of the given property according to the storedValue
     * 
     * @param storedValue value stored for this property that may be transformed.
     * @return the evaluated value for the property based on the given stored value.
     */
    <T> T evaluate(Property<T> property, Object storedValue);
}
