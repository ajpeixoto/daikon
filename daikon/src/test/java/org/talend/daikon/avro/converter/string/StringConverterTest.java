// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit-tests for {@link StringConverter} class
 */
public abstract class StringConverterTest {

    abstract StringConverter<?> createConverter();

    /**
     * Checks {@link StringConverter#getDatumClass()} returns "java.lang.String"
     * {@link Class}
     */
    @Test
    public void testGetDatumClass() {
        StringConverter<?> converter = createConverter();
        Class<?> clazz = converter.getDatumClass();
        assertEquals("java.lang.String", clazz.getCanonicalName());
    }
}
