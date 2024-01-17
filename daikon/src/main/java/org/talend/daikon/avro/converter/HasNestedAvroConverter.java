// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter;

/**
 * Marks that an AvroConverter can contain other {@link AvroConverter}s (for processing elements or fields).
 */
public interface HasNestedAvroConverter<DatumT, AvroT> extends AvroConverter<DatumT, AvroT> {

    Iterable<AvroConverter<?, ?>> getNestedAvroConverters();
}
