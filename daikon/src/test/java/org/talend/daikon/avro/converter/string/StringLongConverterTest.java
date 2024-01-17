// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.avro.Schema;
import org.junit.jupiter.api.Test;
import org.talend.daikon.avro.AvroUtils;

/**
 * Unit tests for {@link StringLongConverter}
 */
public class StringLongConverterTest extends StringConverterTest {

    @Override
    StringLongConverter createConverter() {
        return new StringLongConverter();
    }

    /**
     * Checks {@link StringLongConverter#getSchema()} returns long schema
     */
    @Test
    public void testGetSchema() {
        StringLongConverter converter = createConverter();
        Schema schema = converter.getSchema();
        assertEquals(AvroUtils._long(), schema);
    }

    /**
     * Checks {@link StringLongConverter#convertToDatum(Long)} returns
     * "1234567890", when <code>1234567890<code> is passed
     */
    @Test
    public void testConvertToDatum() {
        StringLongConverter converter = createConverter();
        String value = converter.convertToDatum(1234567890l);
        assertEquals("1234567890", value);
    }

    /**
     * Checks {@link StringLongConverter#convertToAvro(String)} returns
     * <code>1234567890<code>, when "1234567890" is passed
     */
    @Test
    public void testConvertToAvro() {
        StringLongConverter converter = createConverter();
        long value = converter.convertToAvro("1234567890");
        assertEquals(1234567890l, value);
    }

    /**
     * Checks {@link StringLongConverter#convertToAvro(String)} throws
     * {@link NumberFormatException} if not a number string is passed
     */
    @Test
    public void testConvertToAvroNotLong() {
        assertThrows(NumberFormatException.class, () -> {
            StringLongConverter converter = createConverter();
            converter.convertToAvro("not an long");
        });
    }
}
