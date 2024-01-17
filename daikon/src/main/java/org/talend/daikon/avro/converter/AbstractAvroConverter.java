// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter;

import org.apache.avro.Schema;

/**
 * Abstract implementation of {@link AvroConverter}, which implements common methods
 */
public abstract class AbstractAvroConverter<DatumT, AvroT> implements AvroConverter<DatumT, AvroT> {

    /**
     * Class of DI data
     */
    private final Class<DatumT> clazz;

    /**
     * Schema of Avro data
     */
    private final Schema schema;

    /**
     * Sets Avro {@link Schema} and DI {@link Class} of data
     * 
     * @param clazz type of DI data
     * @param schema schema of a Avro data
     */
    public AbstractAvroConverter(Class<DatumT> clazz, Schema schema) {
        this.clazz = clazz;
        this.schema = schema;
    }

    /**
     * Returns {@link Class} of DI data
     */
    @Override
    public Class<DatumT> getDatumClass() {
        return this.clazz;
    }

    /**
     * Returns {@link Schema} of Avro data
     */
    @Override
    public Schema getSchema() {
        return this.schema;
    }
}
