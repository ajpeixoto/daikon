// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.avro.visitor.record;

import org.talend.daikon.avro.visitor.path.TraversalPath;

/**
 * Wrapper for float fields
 */
public class VisitableFloat extends AbstractVisitableStructure<Float> {

    VisitableFloat(Float value, TraversalPath path) {
        super(value, path);
    }

    @Override
    public void accept(RecordVisitor visitor) {
        visitor.visit(this);
    }
}
