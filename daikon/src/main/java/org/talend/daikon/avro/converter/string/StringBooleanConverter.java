// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import org.apache.avro.Schema;
import org.talend.daikon.avro.AvroUtils;

/**
 * Converts String datum to avro boolean type and vice versa
 */
public class StringBooleanConverter extends StringConverter<Boolean> {

    private static final Schema BOOLEAN_SCHEMA = AvroUtils._boolean();

    /**
     * Returns schema of boolean avro type
     * 
     * @return schema of boolean avro type
     */
    @Override
    public Schema getSchema() {
        return BOOLEAN_SCHEMA;
    }

    @Override
    public String convertToDatum(Boolean value) {
        return value.toString();
    }

    @Override
    public Boolean convertToAvro(String value) {
        return Boolean.parseBoolean(value);
    }

}
