// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.inferrer;

import org.apache.avro.Schema;

/**
 * Infers (extracts) Avro {@link Schema} from specific data example (Datum)
 * It can be used to guess schema from the first data arrived to particular part of the system
 */
public interface SchemaInferrer<DatumT> {

    /**
     * Analyzes <code>datum<code> instance and creates Avro schema of this datum
     * 
     * @param datum specific data
     * @return Avro schema of datum
     */
    Schema inferSchema(DatumT datum);
}
