// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import org.apache.avro.Schema;
import org.talend.daikon.avro.AvroUtils;

/**
 * Converts String datum to avro float type and vice versa
 */
public class StringFloatConverter extends StringConverter<Float> {

    private static final Schema FLOAT_SCHEMA = AvroUtils._float();

    /**
     * Returns schema of float avro type
     * 
     * @return schema of float avro type
     */
    @Override
    public Schema getSchema() {
        return FLOAT_SCHEMA;
    }

    @Override
    public String convertToDatum(Float value) {
        return value.toString();
    }

    @Override
    public Float convertToAvro(String value) {
        return Float.valueOf(value);
    }

}
