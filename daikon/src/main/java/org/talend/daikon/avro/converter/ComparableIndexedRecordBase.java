// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.converter;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;

/**
 * A base for {@link IndexedRecord} implementations that respect the Java {@link Object} contracts for {@link #equals},
 * {@link #hashCode} and {@link #compareTo}.
 */
public abstract class ComparableIndexedRecordBase implements IndexedRecord, Comparable<IndexedRecord> {

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof IndexedRecord))
            return false;
        IndexedRecord that = (IndexedRecord) o;
        if (!this.getSchema().equals(that.getSchema()))
            return false;
        // TODO(rskraba): there should be an better&faster compare for Avro!
        return GenericData.get().compare(this, that, getSchema()) == 0;
    }

    @Override
    public int hashCode() {
        return GenericData.get().hashCode(this, getSchema());
    }

    @Override
    public int compareTo(IndexedRecord that) {
        return GenericData.get().compare(this, that, getSchema());
    }

    @Override
    public String toString() {
        return GenericData.get().toString(this);
    }
}