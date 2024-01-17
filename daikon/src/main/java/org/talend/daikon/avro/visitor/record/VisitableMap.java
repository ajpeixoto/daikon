// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.visitor.record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.util.Utf8;
import org.talend.daikon.avro.visitor.path.TraversalPath;

/**
 * Wrapper for maps.
 */
public class VisitableMap extends AbstractVisitableStructure<Map<Utf8, Object>> {

    public VisitableMap(Map<Utf8, Object> value, TraversalPath path) {
        super(value, path);
    }

    @Override
    public void accept(RecordVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * @return an iterator over this map entries.
     */
    public Iterator<VisitableStructure> getValues() {
        final Schema schema = getPath().last().getSchema();
        final Schema valueSchema = schema.getValueType();
        final List<VisitableStructure> entries = new ArrayList<>(this.getValue().size());
        for (Map.Entry<Utf8, Object> entry : this.getValue().entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            TraversalPath path = this.getPath().appendMapEntry(key);
            VisitableStructure child = VisitableStructureFactory.createVisitableStructure(valueSchema, value, path);
            entries.add(child);
        }
        return Collections.unmodifiableList(entries).iterator();
    }
}
