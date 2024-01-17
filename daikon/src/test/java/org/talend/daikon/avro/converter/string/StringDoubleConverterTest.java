// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.avro.Schema;
import org.junit.jupiter.api.Test;
import org.talend.daikon.avro.AvroUtils;

/**
 * Unit tests for {@link StringDoubleConverter}.
 */
public class StringDoubleConverterTest extends StringConverterTest {

    @Override
    StringDoubleConverter createConverter() {
        return new StringDoubleConverter();
    }

    /**
     * Checks {@link StringDoubleConverter#getSchema()} returns double schema
     */
    @Test
    public void testGetSchema() {
        StringDoubleConverter converter = createConverter();
        Schema schema = converter.getSchema();
        assertEquals(AvroUtils._double(), schema);
    }

    /**
     * Checks {@link StringDoubleConverter#convertToDatum(Double)} returns
     * "12.34", when <code>12.34<code> is passed
     */
    @Test
    public void testConvertToDatum() {
        StringDoubleConverter converter = createConverter();
        String value = converter.convertToDatum(12.34);
        assertEquals("12.34", value);
    }

    /**
     * Checks {@link StringDoubleConverter#convertToAvro(String)} returns
     * <code>12.34<code>, when "12.34" is passed
     */
    @Test
    public void testConvertToAvro() {
        StringDoubleConverter converter = createConverter();
        double value = converter.convertToAvro("12.34");
        assertEquals(12.34, value, 0D);
    }

    /**
     * Checks {@link StringDoubleConverter#convertToAvro(String)} throws
     * {@link NumberFormatException} if not a number string is passed
     */
    public void testConvertToAvroNotDouble() {
        assertThrows(NumberFormatException.class, () -> {
            StringDoubleConverter converter = createConverter();
            converter.convertToAvro("not a double");
        });
    }

}
