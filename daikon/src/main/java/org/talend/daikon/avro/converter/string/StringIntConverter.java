// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import org.apache.avro.Schema;
import org.talend.daikon.avro.AvroUtils;

/**
 * Converts String datum to avro int type and vice versa
 */
public class StringIntConverter extends StringConverter<Integer> {

    private static final Schema INT_SCHEMA = AvroUtils._int();

    /**
     * Returns schema of int avro type
     * 
     * @return schema of int avro type
     */
    @Override
    public Schema getSchema() {
        return INT_SCHEMA;
    }

    @Override
    public String convertToDatum(Integer value) {
        return value.toString();
    }

    @Override
    public Integer convertToAvro(String value) {
        return Integer.parseInt(value);
    }

}
