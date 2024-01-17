// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter;

import org.apache.avro.Schema;

/**
 * Callback for code that knows how to convert between a datum type and an Avro-compatible type.
 */
public interface AvroConverter<DatumT, AvroT> {

    /** @return the Avro Schema that is compatible with the AvroT type. */
    Schema getSchema();

    /** @return the class of the specific type that this converter knows how to convert from. */
    Class<DatumT> getDatumClass();

    /** Takes the avro type and converts to the specific type. */
    DatumT convertToDatum(AvroT value);

    /** Takes the specific type and converts to the avro type. */
    AvroT convertToAvro(DatumT value);
}
