// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import org.apache.avro.Schema;
import org.talend.daikon.avro.AvroUtils;

/**
 * Converts String datum to avro long type and vice versa
 */
public class StringLongConverter extends StringConverter<Long> {

    private static final Schema LONG_SCHEMA = AvroUtils._long();

    /**
     * Returns schema of long avro type
     * 
     * @return schema of long avro type
     */
    @Override
    public Schema getSchema() {
        return LONG_SCHEMA;
    }

    @Override
    public String convertToDatum(Long value) {
        return value.toString();
    }

    @Override
    public Long convertToAvro(String value) {
        return Long.parseLong(value);
    }

}
