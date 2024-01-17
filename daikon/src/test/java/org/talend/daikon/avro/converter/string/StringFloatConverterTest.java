// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.avro.Schema;
import org.junit.jupiter.api.Test;
import org.talend.daikon.avro.AvroUtils;

/**
 * Unit tests for {@link StringFloatConverter}.
 */
public class StringFloatConverterTest extends StringConverterTest {

    @Override
    StringFloatConverter createConverter() {
        return new StringFloatConverter();
    }

    /**
     * Checks {@link StringFloatConverter#getSchema()} returns float schema
     */
    @Test
    public void testGetSchema() {
        StringFloatConverter converter = createConverter();
        Schema schema = converter.getSchema();
        assertEquals(AvroUtils._float(), schema);
    }

    /**
     * Checks {@link StringFloatConverter#convertToDatum(Float)} returns
     * "12.34", when <code>12.34f<code> is passed
     */
    @Test
    public void testConvertToDatum() {
        StringFloatConverter converter = createConverter();
        String value = converter.convertToDatum(12.34f);
        assertEquals("12.34", value);
    }

    /**
     * Checks {@link StringFloatConverter#convertToAvro(String)} returns
     * <code>12.34<code>, when "12.34" is passed
     */
    @Test
    public void testConvertToAvro() {
        StringFloatConverter converter = createConverter();
        float value = converter.convertToAvro("12.34");
        assertEquals(12.34f, value, 0f);
    }

    /**
     * Checks {@link StringFloatConverter#convertToAvro(String)} throws
     * {@link NumberFormatException} if not a number string is passed
     */
    @Test
    public void testConvertToAvroNotFloat() {
        assertThrows(NumberFormatException.class, () -> {
            StringFloatConverter converter = createConverter();
            converter.convertToAvro("not a float");
        });
    }
}
