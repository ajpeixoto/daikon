// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter.string;

import org.talend.daikon.avro.converter.AvroConverter;

/**
 * Common abstract field converter for String datum type
 */
public abstract class StringConverter<AvroT> implements AvroConverter<String, AvroT> {

    /**
     * Returns datum class, which is String
     * 
     * @return String.class
     */
    @Override
    public Class<String> getDatumClass() {
        return String.class;
    }

}
