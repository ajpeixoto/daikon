// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import org.apache.avro.Schema;
import org.talend.daikon.avro.AvroUtils;

/**
 * Converts String datum to avro double type and vice versa
 */
public class StringDoubleConverter extends StringConverter<Double> {

    private static final Schema DOUBLE_SCHEMA = AvroUtils._double();

    /**
     * Returns schema of double avro type
     * 
     * @return schema of double avro type
     */
    @Override
    public Schema getSchema() {
        return DOUBLE_SCHEMA;
    }

    @Override
    public String convertToDatum(Double value) {
        return value.toString();
    }

    @Override
    public Double convertToAvro(String value) {
        return Double.valueOf(value);
    }

}
