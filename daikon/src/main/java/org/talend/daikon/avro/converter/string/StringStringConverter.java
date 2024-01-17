// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import org.apache.avro.Schema;
import org.talend.daikon.avro.AvroUtils;

/**
 * Converts String datum to avro string type and vice versa Actually returns
 * arguments unchanged. So it is used as API implementation
 */
public class StringStringConverter extends StringConverter<String> {

    private static final Schema STRING_SCHEMA = AvroUtils._string();

    /**
     * Returns schema of string avro type
     * 
     * @return schema of string avro type
     */
    @Override
    public Schema getSchema() {
        return STRING_SCHEMA;
    }

    @Override
    public String convertToDatum(String value) {
        return value;
    }

    @Override
    public String convertToAvro(String value) {
        return value;
    }

}
